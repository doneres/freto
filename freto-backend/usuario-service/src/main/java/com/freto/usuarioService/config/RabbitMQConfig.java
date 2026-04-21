package com.freto.usuarioService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue usuarioCriadoQueue() {
        return new Queue("usuario.criado", true);
    }

    @Bean
    public DirectExchange fretoExchange() {
        return new DirectExchange("freto.exchange");
    }

    @Bean
    public Binding router(Queue usuarioCriadoQueue, DirectExchange fretoExchange) {
        return BindingBuilder
                .bind(usuarioCriadoQueue)
                .to(fretoExchange)
                .with("usuario.criado");
    }
}
