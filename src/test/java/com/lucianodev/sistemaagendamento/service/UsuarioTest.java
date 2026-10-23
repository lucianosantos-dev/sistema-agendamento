package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.UsuarioCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.UsuarioUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.UsuarioResponse;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import com.lucianodev.sistemaagendamento.enums.Role;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.OperacaoInvalidaException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.factory.UsuarioFactory;
import com.lucianodev.sistemaagendamento.mapper.UsuarioMapper;
import com.lucianodev.sistemaagendamento.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioTest {

    private Usuario usuario;
    private UsuarioCreateDto createDto;
    private UsuarioResponse usuarioResponse;
    private UsuarioUpdateDto usuarioUpdateDto;

    @Mock
    private UsuarioRepository repository;
    @Mock
    private UsuarioMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UsuarioService service;


    @BeforeEach
    public void setUp() {
        usuario = UsuarioFactory.usuarioEntity();
        createDto = UsuarioFactory.usuarioCreateDto();
        usuarioResponse = UsuarioFactory.usuarioResponse();
        usuarioUpdateDto = UsuarioFactory.usuarioUpdateDto("Novo nome atualizado");
    }

    @Test
    public void deveLancarConflictException_QuandoTentarCriarUsuarioComEmailJaCadastrado(){
        when(repository.existsByEmail(usuario.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> service.create(createDto));

        verify(repository, times(1)).existsByEmail(usuario.getEmail());
        verify(repository, never()).save(any());
    }

    @Test
    public void deveCriarUsuarioComSucesso(){
        when(repository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(mapper.toEntity(createDto)).thenReturn(usuario);
        when(passwordEncoder.encode(createDto.senha())).thenReturn("senha_hash_criptografada");
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(usuarioResponse);

        UsuarioResponse result = service.create(createDto);

        assertNotNull(result);
        assertEquals(usuarioResponse.id(), result.id());
        assertEquals(usuarioResponse.email(), result.email());

        verify(repository, times(1)).existsByEmail(usuario.getEmail());
        verify(mapper, times(1)).toEntity(createDto);
        verify(passwordEncoder, times(1)).encode(createDto.senha());
        verify(repository, times(1)).save(usuario);
        verify(mapper, times(1)).toResponse(usuario);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarAtualizarUsuarioComEmailInexistente(){
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(usuario.getEmail(), usuarioUpdateDto));

        verify(repository, times(1)).findByEmail(usuario.getEmail());
        verify(repository, never()).save(any());
    }


    @Test
    public void deveAtualizarUsuarioComSucesso(){
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(usuarioResponse);

        UsuarioResponse result = service.update(usuario.getEmail(), usuarioUpdateDto);
        assertNotNull(result);
        assertEquals(usuarioResponse.email(), result.email());
        assertEquals(usuarioResponse.nome(), result.nome());

        verify(repository, times(1)).findByEmail(usuario.getEmail());
        verify(mapper, times(1)).updateUsuario(usuarioUpdateDto, usuario);
        verify(repository, times(1)).save(usuario);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarBuscarUsuarioComIdInexistente(){
        when(repository.findById(usuario.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(usuario.getId()));

        verify(repository, times(1)).findById(usuario.getId());
    }

    @Test
    public void deveBuscarUsuarioPeloIdComSucesso(){
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(mapper.toResponse(usuario)).thenReturn(usuarioResponse);

        UsuarioResponse result = service.findById(usuario.getId());
        assertNotNull(result);
        assertEquals(usuarioResponse.id(), result.id());

        verify(repository, times(1)).findById(usuario.getId());
        verify(mapper, times(1)).toResponse(usuario);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarDesativarUsuarioComIdInexistente(){
        when(repository.findById(usuario.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.desativar(usuario.getId()));

        verify(repository, times(1)).findById(usuario.getId());
    }

    @Test
    public void deveDesativarUsuarioPeloIdComSucesso(){
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        service.desativar(usuario.getId());

        assertFalse(usuario.getAtivo());

        verify(repository, times(1)).findById(usuario.getId());
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarAtivarUsuarioComIdInexistente(){
        when(repository.findById(usuario.getId())).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class,
                () -> service.ativar(usuario.getId()));

        verify(repository, times(1)).findById(usuario.getId());
    }

    @Test
    public void deveAtivarUsuarioPeloIdComSucesso(){
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        service.ativar(usuario.getId());

        assertTrue(usuario.getAtivo());

        verify(repository, times(1)).findById(usuario.getId());
    }

    @Test
    public void deveLancarOperacaoInvalidaException_QuandoTentarDesativarUsuarioComRoleDeAdmin(){
        usuario.setRole(Role.ADMIN);
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        assertThrows(OperacaoInvalidaException.class,
                () -> service.desativar(usuario.getId()));

        verify(repository, times(1)).findById(usuario.getId());
        assertTrue(usuario.getAtivo());
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarExcluirUsuarioComEmailInexistente(){
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.excluir(usuario.getEmail()));

        verify(repository, times(1)).findByEmail(usuario.getEmail());
        verify(repository, never()).delete(any());
    }

    @Test
    public void deveExcluirUsuarioPeloEmailComSucesso(){
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        service.excluir(usuario.getEmail());

        verify(repository, times(1)).findByEmail(usuario.getEmail());
        verify(repository, times(1)).delete(usuario);
    }
}
