package com.lucianodev.sistemaagendamento.mapper;


import com.lucianodev.sistemaagendamento.dto.request.UsuarioCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.UsuarioUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.UsuarioResponse;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioMapper {

    UsuarioResponse toResponse(Usuario usuario);
    Usuario toEntity(UsuarioCreateDto createDto);
    void updateUsuario(UsuarioUpdateDto updateDto, @MappingTarget Usuario usuario);
}
