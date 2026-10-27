package com.lucianodev.sistemaagendamento.repository;

import com.lucianodev.sistemaagendamento.entity.Cliente;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByUsuario(Usuario usuario);
    Page<Cliente> findAll(Pageable pageable);

    //@Query("SELECT c FROM Cliente c JOIN FETCH c.usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Cliente> findByUsuarioEmail(String email);
}
