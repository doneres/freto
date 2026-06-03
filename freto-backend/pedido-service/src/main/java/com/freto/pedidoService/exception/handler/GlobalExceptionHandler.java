package com.freto.pedidoService.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.freto.pedidoService.exception.ErrorResponse;
import com.freto.pedidoService.exception.PedidoNotFoundException;
import com.freto.pedidoService.exception.PedidoStatusInvalidoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(PedidoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now()));
    }

    @ExceptionHandler(PedidoStatusInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleStatusInvalido(PedidoStatusInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), 400, LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno no servidor.", 500, LocalDateTime.now()));
    }
}
