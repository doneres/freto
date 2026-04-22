package com.freto.notificacaoService.listener;

import com.freto.notificacaoService.service.EmailService;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.freto.notificacaoService.event.UserCreatedEvent;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @RabbitListener(queues = "usuario.criado")
    public void handleUserCreated(UserCreatedEvent event) {
        emailService.sendConfirmationEmail(event);
    }
}