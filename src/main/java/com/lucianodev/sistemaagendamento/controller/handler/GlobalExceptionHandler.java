package com.lucianodev.sistemaagendamento.controller.handler;


import com.lucianodev.sistemaagendamento.dto.CustomErrorDto;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.CredenciaisInvalidasException;
import com.lucianodev.sistemaagendamento.exception.OperacaoInvalidaException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorDto> dataIntegrityViolation(HttpServletRequest request) {
        String msgLimpa = "Conflito de dados: Já existe um registro com essas informações (ex: nome duplicado).";
        return builderResponse(HttpStatus.CONFLICT, msgLimpa, request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomErrorDto> conflict(ConflictException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDto> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(OperacaoInvalidaException.class)
    public ResponseEntity<CustomErrorDto> operacaoInvalida(OperacaoInvalidaException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<CustomErrorDto> credenciaisInvalidas(CredenciaisInvalidasException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDto> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String msgLimpa = e.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação nos campos");
        return builderResponse(HttpStatus.UNPROCESSABLE_CONTENT, msgLimpa, request);
    }

    public ResponseEntity<CustomErrorDto> builderResponse(HttpStatus status, String msg, HttpServletRequest request) {
        CustomErrorDto error = new CustomErrorDto(Instant.now(), status.value(), msg, request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
