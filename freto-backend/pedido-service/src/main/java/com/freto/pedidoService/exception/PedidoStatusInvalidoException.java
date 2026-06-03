package com.freto.pedidoService.exception;

public class PedidoStatusInvalidoException extends RuntimeException {
    public PedidoStatusInvalidoException(String message) {
        super(message);
    }
}
