package com.lucianodev.sistemaagendamento.factory;



import com.lucianodev.sistemaagendamento.dto.request.ProfissionalCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ProfissionalUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ProfissionalResponse;
import com.lucianodev.sistemaagendamento.entity.Profissional;

import java.util.UUID;

public class ProfissionalFactory {

    private static final UUID ID_PADRAO = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public static Profissional createProfissional() {
        return new Profissional(
                ID_PADRAO,
                "Marcos Santos",
                "Barbeiro"
        );
    }

    public static ProfissionalCreateDto profissionalCreateDto() {
        Profissional profissional = createProfissional();

        return new ProfissionalCreateDto(
                profissional.getNome(),
                profissional.getEspecialidade()
        );
    }

    public static ProfissionalUpdateDto profissionalUpdateDto() {
        Profissional profissional = createProfissional();

        return new ProfissionalUpdateDto(
                profissional.getNome(),
                profissional.getEspecialidade()
        );
    }

    public static ProfissionalResponse profissionalResponse() {
        Profissional profissional = createProfissional();

        return new ProfissionalResponse(
                profissional.getId(),
                profissional.getNome(),
                profissional.getEspecialidade()
        );
    }
}
