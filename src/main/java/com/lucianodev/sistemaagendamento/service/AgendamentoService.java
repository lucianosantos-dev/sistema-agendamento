package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.AgendamentoCreateDto;
import com.lucianodev.sistemaagendamento.dto.response.AgendamentoResponse;
import com.lucianodev.sistemaagendamento.entity.*;
import com.lucianodev.sistemaagendamento.enums.StatusAgendamento;
import com.lucianodev.sistemaagendamento.exception.BusinessException;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.mapper.AgendamentoMapper;
import com.lucianodev.sistemaagendamento.repository.*;
import lombok.RequiredArgsConstructor;
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


    @Transactional
    public AgendamentoResponse create(AgendamentoCreateDto dto, String email) {
        validarDadosDeEntrada(dto);

        Cliente cliente = clienteRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Profissional profissional = profissionalRepository.findById(dto.profissionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        List<Servico> servicosEscolhidos = servicoRepository.findAllById(dto.servicosIds());

        if (!profissional.getServicos().containsAll(servicosEscolhidos)) {
            throw new BusinessException("Serviço(s) indisponível/indisponíveis  para o profissional escolhido.");
        }

        if (servicosEscolhidos.size() != dto.servicosIds().size()) {
            throw new ResourceNotFoundException("Serviço(s) indisponível/indisponíveis para o profissional com ID selecionado.");
        }

        Agendamento agendamento = Agendamento.criar(
                cliente,
                profissional,
                dto.inicio(),
                servicosEscolhidos,
                LocalDateTime.now(clock)
        );

        validarExpedientes(agendamento);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        return agendamentoMapper.toResponse(salvo);
    }

    private void validarDadosDeEntrada(AgendamentoCreateDto dto) {
        if (dto.servicosIds().stream().distinct().count() != dto.servicosIds().size()) {
            throw new BusinessException("A lista de serviços não pode ter IDs repetidos.");
        }

        if (dto.inicio().isBefore(LocalDateTime.now(clock))) {
            throw new BusinessException("A data de inicio não pode ser no passado.");
        }

        if (dto.inicio().getMinute() % 15 != 0) {
            throw new BusinessException("Intervalo de agendamento inválido. Use grades de 15 minutos.");
        }

        if (dto.inicio().getSecond() != 0 || dto.inicio().getNano() != 0) {
            throw new BusinessException("O horário de inicio deve ter os segundos zerados (ex: 18:30:00).");
        }
    }

    private void validarExpedientes(Agendamento agendamento) {
        LocalDateTime inicio = agendamento.getDataHoraInicio();
        LocalDateTime fim = agendamento.getDataHoraFim();
        UUID profissionalId = agendamento.getProfissional().getId();

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

        List<StatusAgendamento> statusBloqueantes = agendamento.bloqueantes();

        if (agendamentoRepository.existsConflictParaProfissional(profissionalId, inicio, fim, statusBloqueantes)) {
            throw new ConflictException("O profissional selecionado já possui um agendamento nesse horário.");
        }
        if (agendamentoRepository.existsByClienteIdAndStatusIn(agendamento.getCliente().getId(), statusBloqueantes)) {
            throw new ConflictException("Já existe um agendamento para o cliente com id: " + agendamento.getCliente().getId());
        }

    }
}
