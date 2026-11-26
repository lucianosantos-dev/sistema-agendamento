package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.AgendamentoCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.AgendamentoUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.AgendamentoResponse;
import com.lucianodev.sistemaagendamento.entity.*;
import com.lucianodev.sistemaagendamento.enums.StatusAgendamento;
import com.lucianodev.sistemaagendamento.exception.BusinessException;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.mapper.AgendamentoMapper;
import com.lucianodev.sistemaagendamento.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ExpedienteRepository expedienteRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final Clock clock;

    private static final int TOLERANCIA_MINUTOS = 15;
    private static final int MINUTOS_ANTES_CHEGADA = 30;


    @Transactional
    public AgendamentoResponse create(AgendamentoCreateDto dto, String email) {
        validarDadosDeEntrada(dto.inicio(), dto.servicosIds());

        DadosAgendamento dados = buscarEvalidarProfissionalClienteEServicos(dto.profissionalId(), email, dto.servicosIds());

        Agendamento agendamento = Agendamento.criar(
                dados.cliente,
                dados.profissional,
                dto.inicio(),
                dados.servicos,
                LocalDateTime.now(clock)
        );

        validarExpedientes(agendamento);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        return agendamentoMapper.toResponse(salvo);
    }

    @Transactional
    public AgendamentoResponse atualizarAgendamento(UUID agendamentoId, AgendamentoUpdateDto dto, String email) {
        validarDadosDeEntrada(dto.novoInicio(), dto.servicosIds());

        DadosAgendamento dados = buscarEvalidarProfissionalClienteEServicos(dto.profissionalId(), email, dto.servicosIds());

        Agendamento agendamento = agendamentoRepository.findByIdAndClienteUsuarioEmail(agendamentoId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));


        agendamento.remarcar(dados.profissional, dto.novoInicio(), dados.servicos);

        validarExpedientes(agendamento);

        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }


    @Transactional(readOnly = true)
    public Page<AgendamentoResponse> findAllByCliente(String email, Pageable pageable) {
        if (!clienteRepository.existsByUsuarioEmail(email)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return agendamentoRepository.findByClienteUsuarioEmail(email, pageable)
                .map(agendamentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AgendamentoResponse> findByDataHoraInicioBetween(LocalDateTime dataHoraInicio,
                                                                 LocalDateTime dataHoraFim,
                                                                 Pageable pageable) {

        return agendamentoRepository.findByDataHoraInicioBetween(dataHoraInicio, dataHoraFim, pageable)
                .map(agendamentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AgendamentoResponse> findAllByAdmin(Pageable pageable) {
        return agendamentoRepository.findAll(pageable)
                .map(agendamentoMapper::toResponse);
    }


    @Transactional(readOnly = true)
    public AgendamentoResponse findByIdAdmin(UUID id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        return agendamentoMapper.toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse findByIdAndCliente(UUID id, String email) {
        Agendamento agendamento = agendamentoRepository.findByIdAndClienteUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        return agendamentoMapper.toResponse(agendamento);
    }

    @Transactional
    public void cancelarAgendamentoComoCliente(UUID agendamentoId, String emailUsuario) {
        Agendamento ag = agendamentoRepository.findByIdAndClienteUsuarioEmail(agendamentoId, emailUsuario)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agendamento não encontrado."));

        LocalDateTime agora = LocalDateTime.now(clock);

        if (ag.getDataHoraInicio().isBefore(agora.plusHours(2))) {
            throw new BusinessException("Cancelamento só com 2 horas de antecedência");
        }

        ag.cancelar(agora);
    }

    //Admin pode cancelar agendamento a qualquer momento
    @Transactional
    public void cancelarAgendamentoComoAdmin(UUID agendamentoId) {
        Agendamento ag = buscarPorId(agendamentoId);

        ag.cancelar(LocalDateTime.now(clock));
    }

    @Transactional
    public void registrarChegada(UUID agendamentoId) {
        Agendamento ag = buscarPorId(agendamentoId);

        LocalDateTime agora = LocalDateTime.now(clock);
        LocalDateTime inicio = ag.getDataHoraInicio();

        if (agora.isBefore(inicio.minusMinutes(MINUTOS_ANTES_CHEGADA))) {
            throw new BusinessException("A chegada só pode ser registrada a partir de 30 minutos antes do horário marcado.");
        }
        if (agora.isAfter(inicio.plusMinutes(TOLERANCIA_MINUTOS))) {
            throw new
                    BusinessException("A tolêrancia de 15 minutos já passou.");
        }

        ag.registrarChegada(agora);
    }

    @Transactional
    public void registrarFalta(UUID agendamentoId) {
        Agendamento ag = buscarPorId(agendamentoId);

        LocalDateTime agora = LocalDateTime.now(clock);

        if (agora.isBefore(ag.getDataHoraInicio().plusMinutes(TOLERANCIA_MINUTOS))) {
            throw new BusinessException("Só pode registrar falta após 15 minutos do horário marcado.");
        }

        ag.registrarFalta();
    }

    @Transactional
    public void concluir(UUID agendamentoId) {
        Agendamento ag = buscarPorId(agendamentoId);
        ag.concluir();
    }

    private Agendamento buscarPorId(UUID agendamentoId) {
        return agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agendamento não encontrado com Id: " + agendamentoId));
    }

    private void validarDadosDeEntrada(LocalDateTime inicio, List<UUID> servicosIds) {
        if (servicosIds.stream().distinct().count() != servicosIds.size()) {
            throw new BusinessException("A lista de serviços não pode ter IDs repetidos.");
        }

        if (inicio.isBefore(LocalDateTime.now(clock))) {
            throw new BusinessException("A data de inicio não pode ser no passado.");
        }

        if (inicio.getMinute() % 15 != 0) {
            throw new BusinessException("Intervalo de agendamento inválido. Use grades de 15 minutos.");
        }

        if (inicio.getSecond() != 0 || inicio.getNano() != 0) {
            throw new BusinessException("O horário de inicio deve ter os segundos zerados (ex: 18:30:00).");
        }
    }

    private void validarExpedientes(Agendamento agendamento) {
        UUID id = agendamento.getId();
        UUID clientId = agendamento.getCliente().getId();
        UUID profissionalId = agendamento.getProfissional().getId();
        LocalDateTime inicio = agendamento.getDataHoraInicio();
        LocalDateTime fim = agendamento.getDataHoraFim();
        List<StatusAgendamento> statusBloqueantes = agendamento.bloqueantes();


        boolean conflitoProfissional = (id == null)
                ? agendamentoRepository.existsConflictParaProfissional(profissionalId, inicio, fim, statusBloqueantes)
                : agendamentoRepository.existsConflictParaProfissionalIgnorandoId(id, profissionalId, inicio, fim, statusBloqueantes);

        if (conflitoProfissional) {
            throw new ConflictException("O profissional selecionado já possui um agendamento nesse horário.");
        }

        boolean conflitoCliente = (id == null)
                ? agendamentoRepository.existsConflitoHorarioCliente(clientId, inicio, fim, statusBloqueantes)
                : agendamentoRepository.existsConflitoHorarioIgnorandoId(id, clientId, inicio, fim, statusBloqueantes);

        if (conflitoCliente) {
            throw new ConflictException("O cliente com id " + agendamento.getCliente().getId() + " já possui um agendamento neste intervalo de horário.");
        }

        if (!inicio.toLocalDate().equals(fim.toLocalDate())) {
            throw new BusinessException("O agendamento não pode iniciar em um dia e terminar no dia seguinte.");
        }


        List<Expediente> expedientes = expedienteRepository
                .findAllByProfissionalIdAndDiaSemana(profissionalId, inicio.getDayOfWeek());

        boolean cabeNoExpediente = expedientes.stream().anyMatch(expediente ->
                !inicio.toLocalTime().isBefore(expediente.getHoraInicio())
                        && !fim.toLocalTime().isAfter(expediente.getHoraFim())
        );

        if (!cabeNoExpediente) {
            throw new BusinessException("Fora do horário de atendimento.");
        }

    }

    private record DadosAgendamento(Profissional profissional, Cliente cliente, List<Servico> servicos) {
    }

    private DadosAgendamento buscarEvalidarProfissionalClienteEServicos(UUID profissionalId, String email, List<UUID> servicosIds) {
        Cliente cliente = clienteRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));


        List<Servico> servicosEscolhidos = servicoRepository.findAllById(servicosIds);


        if (servicosEscolhidos.size() != servicosIds.size()) {
            throw new ResourceNotFoundException("Um ou mais serviços informados não foram encontrados.");
        }

        if (!profissional.getServicos().containsAll(servicosEscolhidos)) {
            throw new BusinessException("Serviço(s) indisponível/indisponíveis  para o profissional escolhido.");
        }

        return new DadosAgendamento(profissional, cliente, servicosEscolhidos);
    }
}
