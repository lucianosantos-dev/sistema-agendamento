package com.lucianodev.sistemaagendamento.mapper;

import com.lucianodev.sistemaagendamento.dto.request.AgendamentoCreateDto;
import com.lucianodev.sistemaagendamento.dto.response.AgendamentoResponse;
import com.lucianodev.sistemaagendamento.entity.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {


    Agendamento toEntity(AgendamentoCreateDto dto);

    @Mapping(source = "profissional.nome", target = "nomeProfissional")
    @Mapping(source = "dataHoraInicio", target = "inicio")
    @Mapping(source = "dataHoraFim", target = "fim")
    AgendamentoResponse toResponse(Agendamento entity);
}
