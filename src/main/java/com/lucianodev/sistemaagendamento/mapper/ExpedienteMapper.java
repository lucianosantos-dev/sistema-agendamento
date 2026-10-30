package com.lucianodev.sistemaagendamento.mapper;

import com.lucianodev.sistemaagendamento.dto.request.ExpedienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.response.ExpedienteResponse;
import com.lucianodev.sistemaagendamento.entity.Expediente;
import com.lucianodev.sistemaagendamento.entity.Profissional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
public interface ExpedienteMapper {

    @Mapping(target = "id", ignore = true)
    Expediente toEntity(ExpedienteCreateDto createDto, Profissional profissional);

    @Mapping(target = "profissionalId", source = "profissional.id")
    ExpedienteResponse toResponse(Expediente entity);
}
