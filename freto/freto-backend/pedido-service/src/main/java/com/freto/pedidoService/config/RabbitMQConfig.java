package com.freto.pedidoService.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "freto.exchange";
    public static final String QUEUE_PEDIDO_CRIADO = "pedido.criado";
    public static final String QUEUE_PEDIDO_STATUS = "pedido.status.atualizado";
    public static final String ROUTING_PEDIDO_CRIADO = "pedido.criado";
    public static final String ROUTING_PEDIDO_STATUS = "pedido.status.atualizado";

    @Bean
    public DirectExchange fretoExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue pedidoCriadoQueue() {
        return new Queue(QUEUE_PEDIDO_CRIADO, true);
    }

    @Bean
    public Queue pedidoStatusQueue() {
        return new Queue(QUEUE_PEDIDO_STATUS, true);
    }

    @Bean
    public Binding bindPedidoCriado(Queue pedidoCriadoQueue, DirectExchange fretoExchange) {
        return BindingBuilder.bind(pedidoCriadoQueue).to(fretoExchange).with(ROUTING_PEDIDO_CRIADO);
    }

    @Bean
    public Binding bindPedidoStatus(Queue pedidoStatusQueue, DirectExchange fretoExchange) {
        return BindingBuilder.bind(pedidoStatusQueue).to(fretoExchange).with(ROUTING_PEDIDO_STATUS);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
