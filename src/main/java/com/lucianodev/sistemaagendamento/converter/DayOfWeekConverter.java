package com.lucianodev.sistemaagendamento.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;

@Converter(autoApply = true)
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {


    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dia) {
        if (dia == null) return null;
        return dia.getValue();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer numeroBanco) {
        if (numeroBanco == null) return null;
        return DayOfWeek.of(numeroBanco);
    }
}
