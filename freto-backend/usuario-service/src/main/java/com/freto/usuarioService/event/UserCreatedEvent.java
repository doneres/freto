package com.freto.usuarioService.event;

public record UserCreatedEvent(
        String name,
        String email) {
}
