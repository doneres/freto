package com.freto.pedidoService.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishPedidoCriado(PedidoCriadoEvent event) {
        rabbitTemplate.convertAndSend("freto.exchange", "pedido.criado", event);
    }

    public void publishStatusAtualizado(PedidoStatusAtualizadoEvent event) {
        rabbitTemplate.convertAndSend("freto.exchange", "pedido.status.atualizado", event);
    }
}
