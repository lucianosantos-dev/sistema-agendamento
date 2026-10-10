package com.lucianodev.sistemaagendamento.mapper;


import com.lucianodev.sistemaagendamento.dto.request.ServicoCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ServicoUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ServicoResponse;
import com.lucianodev.sistemaagendamento.entity.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ServicoMapper {

    ServicoResponse toResponse(Servico entity);
    Servico toEntity(ServicoCreateDto servicoCreateDto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(ServicoUpdateDto updateDto, @MappingTarget Servico servico);
}
