package com.freto.pedidoService.messaging;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.freto.pedidoService.config.RabbitMQConfig;
import com.freto.pedidoService.dto.PedidoResponseDTO;
import com.freto.pedidoService.event.PedidoCriadoEvent;
import com.freto.pedidoService.model.Pedido;
import com.freto.pedidoService.model.enums.PedidoStatus;
import com.freto.pedidoService.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Consumidor responsável pelo matching assíncrono entre pedido e motorista.
 * Ao receber um pedido na fila, simula a busca por um motorista disponível
 * e atualiza o status, notificando o cliente via WebSocket.
 *
 * Padrão aplicado: Consumer Pattern (Mensageria) + Observer (WebSocket)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoMatchConsumer {

    private final PedidoRepository pedidoRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PEDIDO_CRIADO)
    public void processarPedido(PedidoCriadoEvent event) {
        log.info("Pedido recebido na fila: {}", event.getPedidoId());

        // Delega o trabalho bloqueante a uma thread separada para não segurar o consumer
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Matching interrompido para pedido {}", event.getPedidoId());
                return;
            }

            pedidoRepository.findById(event.getPedidoId()).ifPresent(pedido -> {
                UUID motoristaSimulado = UUID.randomUUID();

                pedido.setStatus(PedidoStatus.MOTORISTA_ENCONTRADO);
                pedido.setMotoristaId(motoristaSimulado);
                pedido.setAtualizadoEm(LocalDateTime.now());
                pedidoRepository.save(pedido);

                log.info("Motorista encontrado para pedido {}: motorista {}", pedido.getId(), motoristaSimulado);

                PedidoResponseDTO response = toResponseDTO(pedido);
                messagingTemplate.convertAndSend("/topic/pedidos/" + pedido.getId(), response);
            });
        });
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PEDIDO_STATUS)
    public void processarAtualizacaoStatus(com.freto.pedidoService.event.PedidoStatusAtualizadoEvent event) {
        log.info("Status atualizado para pedido {}: {}", event.getPedidoId(), event.getNovoStatus());

        pedidoRepository.findById(event.getPedidoId()).ifPresent(pedido -> {
            PedidoResponseDTO response = toResponseDTO(pedido);
            messagingTemplate.convertAndSend("/topic/pedidos/" + pedido.getId(), response);
        });
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getMotoristaId(),
                pedido.getOrigem(),
                pedido.getDestino(),
                pedido.getDescricao(),
                pedido.getStatus(),
                pedido.getCriadoEm(),
                pedido.getAtualizadoEm()
        );
    }
}
