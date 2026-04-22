package com.freto.notificacaoService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.freto.notificacaoService.event.UserCreatedEvent;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendConfirmationEmail(UserCreatedEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@doneres.dev");
            helper.setTo(event.email());
            helper.setSubject("Bem-vindo ao FRETO!");
            helper.setText(
                    "<h1>Olá, " + event.name() + "!</h1>" +
                            "<p>Sua conta foi criada com sucesso!</p>" +
                            "<p>Bem-vindo ao FRETO — frete rápido, sem stress, sem surpresa.</p>",
                    true // true = é HTML
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email: " + e.getMessage(), e);
        }
    }
}