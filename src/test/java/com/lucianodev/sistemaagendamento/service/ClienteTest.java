package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.ClienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ClienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ClienteResponse;
import com.lucianodev.sistemaagendamento.entity.Cliente;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import com.lucianodev.sistemaagendamento.enums.Role;
import com.lucianodev.sistemaagendamento.exception.AcessoNegadoException;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.factory.ClienteFactory;
import com.lucianodev.sistemaagendamento.factory.UsuarioFactory;
import com.lucianodev.sistemaagendamento.mapper.ClienteMapper;
import com.lucianodev.sistemaagendamento.repository.ClienteRepository;
import com.lucianodev.sistemaagendamento.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteTest {

    private Cliente cliente;
    private ClienteCreateDto createDto;
    private ClienteResponse clienteResponse;
    private ClienteUpdateDto updateDto;
    private Usuario usuario;
    private String emailFake;

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ClienteMapper clienteMapper;
    @InjectMocks
    private ClienteService clienteService;


    @BeforeEach
    public void setUp() {
        cliente = ClienteFactory.createCliente();
        createDto = ClienteFactory.createDto();
        clienteResponse = ClienteFactory.clienteResponse();
        updateDto = ClienteFactory.updateDto("73940028977", LocalDate.of(1990, 1, 1));
        usuario = UsuarioFactory.usuarioEntity();
        emailFake = "emailfake@gmail.com";
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarCriarClienteComEmailDeUsuarioQueNaoExiste() {
        when(usuarioRepository.findByEmail(emailFake)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clienteService.create(createDto, emailFake));

        verify(usuarioRepository, times(1)).findByEmail(emailFake);
        verify(clienteRepository, never()).existsByUsuario(any());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void deveLancarAcessoNegadoException_QuandoTentarCriarClienteComRoleAdmin() {
        usuario.setRole(Role.ADMIN);
        when(usuarioRepository.findByEmail(emailFake)).thenReturn(Optional.of(usuario));

        assertThrows(AcessoNegadoException.class,
                () -> clienteService.create(createDto, emailFake));


        verify(usuarioRepository, times(1)).findByEmail(emailFake);
        verify(clienteRepository, never()).existsByUsuario(any());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void deveLancarConflictException_QuandoTentarCriarClienteComPerfilDeUsuarioQueJaExiste() {
        usuario.setRole(Role.CLIENTE);
        when(usuarioRepository.findByEmail(emailFake)).thenReturn(Optional.of(usuario));
        when(clienteRepository.existsByUsuario(usuario)).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> clienteService.create(createDto, emailFake));

        verify(usuarioRepository, times(1)).findByEmail(emailFake);
        verify(clienteRepository, times(1)).existsByUsuario(usuario);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void deveCriarClienteComSucesso() {
        usuario.setRole(Role.CLIENTE);
        when(usuarioRepository.findByEmail(emailFake)).thenReturn(Optional.of(usuario));
        when(clienteRepository.existsByUsuario(usuario)).thenReturn(false);
        when(clienteMapper.toEntity(createDto, usuario)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

        ClienteResponse result = clienteService.create(createDto, emailFake);

        assertNotNull(result);
        assertEquals(clienteResponse.id(), result.id());
        assertEquals(clienteResponse.cpf(), result.cpf());
        assertEquals(clienteResponse.usuarioId(), result.usuarioId());

        verify(usuarioRepository, times(1)).findByEmail(emailFake);
        verify(clienteRepository, times(1)).existsByUsuario(usuario);
        verify(clienteMapper, times(1)).toEntity(createDto, usuario);
        verify(clienteRepository, times(1)).save(cliente);
        verify(clienteMapper, times(1)).toResponse(cliente);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarAtualizarClienteComEmailQueNaoExiste() {
        when(clienteRepository.findByUsuarioEmail(emailFake)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clienteService.update(updateDto, emailFake));

        verify(clienteRepository, times(1)).findByUsuarioEmail(emailFake);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void deveLancarAcessoNegadoException_QuandoTentarAtualizarUsuarioComEmailDiferente() {
        String emailInvasor = "emailinvasor@email.com";

        Usuario usuarioDono = UsuarioFactory.usuarioEntity();
        usuarioDono.setEmail(emailFake);

        Cliente clienteDono = ClienteFactory.createCliente(usuarioDono);
        when(clienteRepository.findByUsuarioEmail(emailInvasor)).thenReturn(Optional.of(clienteDono));

        assertThrows(AcessoNegadoException.class,
                () -> clienteService.update(updateDto, emailInvasor));

        verify(clienteRepository, times(1)).findByUsuarioEmail(emailInvasor);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void deveAtualizarClienteComSucesso() {
        Usuario usuarioDono = UsuarioFactory.usuarioEntity();
        usuarioDono.setEmail(emailFake);
        Cliente clienteDono = ClienteFactory.createCliente(usuarioDono);

        when(clienteRepository.findByUsuarioEmail(emailFake)).thenReturn(Optional.of(clienteDono));
        when(clienteRepository.save(clienteDono)).thenReturn(clienteDono);
        when(clienteMapper.toResponse(clienteDono)).thenReturn(clienteResponse);

        ClienteResponse result = clienteService.update(updateDto, emailFake);
        assertNotNull(result);
        assertEquals(clienteResponse.id(), result.id());
        assertEquals(clienteResponse.usuarioId(), result.usuarioId());

        verify(clienteRepository, times(1)).findByUsuarioEmail(emailFake);
        verify(clienteRepository, times(1)).save(clienteDono);
        verify(clienteMapper, times(1)).updateEntity(updateDto, clienteDono);
        verify(clienteMapper, times(1)).toResponse(clienteDono);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarBuscarClienteComEmailInexistente(){
        when(clienteRepository.findByUsuarioEmail(emailFake)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class,
                ()-> clienteService.findByEmail(emailFake));

        verify(clienteRepository, times(1)).findByUsuarioEmail(emailFake);
    }

    @Test
    public void deveBuscarClientePorEmailComSucesso(){
        when(clienteRepository.findByUsuarioEmail(emailFake)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

        ClienteResponse result = clienteService.findByEmail(emailFake);
        assertNotNull(result);
        assertEquals(clienteResponse.id(), result.id());
        assertEquals(clienteResponse.usuarioId(), result.usuarioId());

        verify(clienteRepository, times(1)).findByUsuarioEmail(emailFake);
        verify(clienteMapper, times(1)).toResponse(cliente);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarBuscarClienteComIdInexistente(){
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clienteService.findById(cliente.getId()));

        verify(clienteRepository, times(1)).findById(cliente.getId());
    }

    @Test
    public void deveBuscarClientePorIdComSucesso(){
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

        ClienteResponse result = clienteService.findById(cliente.getId());
        assertNotNull(result);
        assertEquals(clienteResponse.id(), result.id());
        assertEquals(clienteResponse.usuarioId(), result.usuarioId());

        verify(clienteRepository, times(1)).findById(cliente.getId());
        verify(clienteMapper, times(1)).toResponse(cliente);
    }
}
