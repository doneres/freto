package com.freto.notificacaoService.event;

public record UserCreatedEvent(
        String name,
        String email) {
}
