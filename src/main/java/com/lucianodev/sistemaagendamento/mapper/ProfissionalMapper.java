package com.lucianodev.sistemaagendamento.mapper;


import com.lucianodev.sistemaagendamento.dto.request.ProfissionalCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ProfissionalUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ProfissionalResponse;
import com.lucianodev.sistemaagendamento.entity.Profissional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfissionalMapper {

    ProfissionalResponse toResponse(Profissional entity);
    Profissional toEntity(ProfissionalCreateDto profissionalCreateDto);

    @Mapping(target = "id", ignore = true)
    void update(ProfissionalUpdateDto updateDto, @MappingTarget Profissional entity);
}
