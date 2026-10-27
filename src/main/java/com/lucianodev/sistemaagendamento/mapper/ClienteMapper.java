package com.lucianodev.sistemaagendamento.mapper;

import com.lucianodev.sistemaagendamento.dto.request.ClienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ClienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ClienteResponse;
import com.lucianodev.sistemaagendamento.entity.Cliente;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "Spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", source = "usuario")
    Cliente toEntity(ClienteCreateDto dto, Usuario usuario);


    @Mapping(target = "cpf", qualifiedByName = "mascararCpf")
    @Mapping(target = "usuarioId", source = "usuario.id")
    ClienteResponse toResponse(Cliente cliente);

    @Named("mascararCpf")
    default String mascararCpf(String cpf) {
        if (cpf == null || cpf.length() < 11) {
            return "***.***.***-**";
        }
        String apenasNumeros = cpf.replaceAll("\\D", "");
        if (apenasNumeros.length() != 11) {
            return cpf;
        }
        return "***." + apenasNumeros.substring(3, 6) + "." + apenasNumeros.substring(6, 9) + "-**";
    }

    void updateEntity(ClienteUpdateDto updateDto, @MappingTarget Cliente cliente);
}

