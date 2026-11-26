package com.lucianodev.sistemaagendamento.repository;

import com.lucianodev.sistemaagendamento.entity.Agendamento;
import com.lucianodev.sistemaagendamento.enums.StatusAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {


    @EntityGraph(attributePaths = {"profissional", "cliente", "servicos", "servicos.servico"})
    Optional<Agendamento> findByIdAndClienteUsuarioEmail(UUID id, String email);

    Page<Agendamento> findByDataHoraInicioBetween(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Pageable pageable);

    @EntityGraph(attributePaths = "profissional")
    Page<Agendamento> findByClienteUsuarioEmail(String email, Pageable pageable);


    //Validações de conflito para novo Agendamento//


    @Query("""
            SELECT COUNT(a) > 0
            FROM Agendamento a
            WHERE a.cliente.id = :clienteId
            AND a.status IN :status
            AND a.dataHoraInicio < :fim
            AND a.dataHoraFim > :inicio
            """)
    boolean existsConflitoHorarioCliente(@Param("clienteId") UUID clienteId,
                                  @Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim,
                                  @Param("status") List<StatusAgendamento> status);


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

    //Validações de conflito para atualizações//

    @Query("""
            SELECT COUNT(a) > 0
            FROM Agendamento a
            WHERE a.cliente.id = :clienteId
            AND a.id <> :agendamentoId
            AND a.status IN :status
            AND a.dataHoraInicio < :fim
            AND a.dataHoraFim > :inicio
            """)
    boolean existsConflitoHorarioIgnorandoId(@Param("agendamentoId") UUID agendamentoId,
                                             @Param("clienteId") UUID clienteId,
                                             @Param("inicio") LocalDateTime inicio,
                                             @Param("fim") LocalDateTime fim,
                                             @Param("status") List<StatusAgendamento> status);

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM Agendamento a
            WHERE a.profissional.id = :profissionalId
            AND a.id <> :agendamentoId
            AND a.status IN :statusBloqueantes
            AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)
            """)
    boolean existsConflictParaProfissionalIgnorandoId(
            @Param("agendamentoId") UUID agendamentoId,
            @Param("profissionalId") UUID profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusBloqueantes") List<StatusAgendamento> statusBloqueantes
    );
}
