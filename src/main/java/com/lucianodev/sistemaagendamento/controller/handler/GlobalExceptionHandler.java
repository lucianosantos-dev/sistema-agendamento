package com.lucianodev.sistemaagendamento.controller.handler;


import com.lucianodev.sistemaagendamento.dto.CustomErrorDto;
import com.lucianodev.sistemaagendamento.dto.FieldMessageDto;
import com.lucianodev.sistemaagendamento.dto.ValidationErrorDto;
import com.lucianodev.sistemaagendamento.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

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

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<CustomErrorDto> acessoNegado(AcessoNegadoException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorDto> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(HorarioInvalidoException.class)
    public ResponseEntity<CustomErrorDto> horarioInvalido(HorarioInvalidoException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<CustomErrorDto> credenciaisInvalidas(CredenciaisInvalidasException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CustomErrorDto> business(BusinessException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldMessageDto> fieldMessage = e.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldMessageDto(err.getField(), err.getDefaultMessage()))
                .toList();

        ValidationErrorDto errorDto = new ValidationErrorDto(
                Instant.now(),
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Dados invalídos",
                request.getRequestURI(),
                fieldMessage
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(errorDto);
    }

    public ResponseEntity<CustomErrorDto> builderResponse(HttpStatus status, String msg, HttpServletRequest request) {
        CustomErrorDto error = new CustomErrorDto(Instant.now(), status.value(), msg, request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

}
