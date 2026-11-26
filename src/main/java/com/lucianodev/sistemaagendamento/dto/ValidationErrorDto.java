package com.lucianodev.sistemaagendamento.dto;

import java.time.Instant;
import java.util.List;

public record ValidationErrorDto(
        Instant timestamp,
        Integer status,
        String message,
        String path,
        List<FieldMessageDto> errors
) {
}
