package com.lucianodev.sistemaagendamento.repository;

import com.lucianodev.sistemaagendamento.entity.Agendamento;
import com.lucianodev.sistemaagendamento.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    boolean existsByClienteIdAndStatusIn(UUID clienteId, List<StatusAgendamento> status);


    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM Agendamento a
            WHERE a.profissional.id = :profissionalId
            AND a.status IN :statusBloqueantes
            AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)
            """)
    boolean existsConflictParaProfissional(
            @Param("profissionalId") UUID profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusBloqueantes") List<StatusAgendamento> statusBloqueantes
    );
}
