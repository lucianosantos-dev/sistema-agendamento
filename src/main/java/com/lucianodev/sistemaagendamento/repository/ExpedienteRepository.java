package com.lucianodev.sistemaagendamento.repository;

import com.lucianodev.sistemaagendamento.entity.Expediente;
import com.lucianodev.sistemaagendamento.entity.Profissional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpedienteRepository extends JpaRepository<Expediente, UUID> {

    boolean existsByProfissionalAndDiaSemana(Profissional profissional, DayOfWeek diaSemana);
    Page<Expediente> findAll(Pageable pageable);
    List<Expediente> findAllByProfissionalIdOrderByDiaSemanaAscHoraInicioAsc(UUID profissionalId);
}
