package com.freto.pedidoService.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.freto.pedidoService.dto.AtualizarStatusDTO;
import com.freto.pedidoService.dto.CreatePedidoDTO;
import com.freto.pedidoService.dto.PedidoResponseDTO;
import com.freto.pedidoService.event.PedidoCriadoEvent;
import com.freto.pedidoService.event.PedidoEventPublisher;
import com.freto.pedidoService.event.PedidoStatusAtualizadoEvent;
import com.freto.pedidoService.exception.PedidoNotFoundException;
import com.freto.pedidoService.exception.PedidoStatusInvalidoException;
import com.freto.pedidoService.model.Pedido;
import com.freto.pedidoService.model.enums.PedidoStatus;
import com.freto.pedidoService.repository.PedidoRepository;
import com.freto.pedidoService.service.PedidoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoEventPublisher pedidoEventPublisher;

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

    @Override
    public PedidoResponseDTO criarPedido(CreatePedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.clienteId());
        pedido.setOrigem(dto.origem());
        pedido.setDestino(dto.destino());
        pedido.setDescricao(dto.descricao());

        Pedido salvo = pedidoRepository.save(pedido);

        // Publica evento na fila para matching assíncrono
        pedidoEventPublisher.publishPedidoCriado(new PedidoCriadoEvent(
                salvo.getId(),
                salvo.getClienteId(),
                salvo.getOrigem(),
                salvo.getDestino(),
                salvo.getDescricao()
        ));

        return toResponseDTO(salvo);
    }

    @Override
    public PedidoResponseDTO buscarPorId(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado: " + id));
        return toResponseDTO(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listarPorCliente(UUID clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> listarAguardando() {
        return pedidoRepository.findByStatus(PedidoStatus.AGUARDANDO_MOTORISTA)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO atualizarStatus(UUID id, AtualizarStatusDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado: " + id));

        // Valida transição de status (Strategy Pattern implícito)
        validarTransicao(pedido.getStatus(), dto.status());

        pedido.setStatus(dto.status());
        pedido.setAtualizadoEm(LocalDateTime.now());

        if (dto.motoristaId() != null) {
            pedido.setMotoristaId(dto.motoristaId());
        }

        Pedido atualizado = pedidoRepository.save(pedido);

        // Publica evento de atualização (consumido pelo WebSocket notifier)
        pedidoEventPublisher.publishStatusAtualizado(new PedidoStatusAtualizadoEvent(
                atualizado.getId(),
                atualizado.getStatus(),
                atualizado.getMotoristaId()
        ));

        return toResponseDTO(atualizado);
    }

    @Override
    public void cancelarPedido(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado: " + id));

        if (pedido.getStatus() == PedidoStatus.ENTREGUE) {
            throw new PedidoStatusInvalidoException("Pedidos já entregues não podem ser cancelados.");
        }

        pedido.setStatus(PedidoStatus.CANCELADO);
        pedido.setAtualizadoEm(LocalDateTime.now());
        pedidoRepository.save(pedido);

        pedidoEventPublisher.publishStatusAtualizado(new PedidoStatusAtualizadoEvent(
                pedido.getId(), PedidoStatus.CANCELADO, pedido.getMotoristaId()
        ));
    }

    /**
     * Strategy de validação de transição de status.
     * Garante que o fluxo de estados seja coerente.
     */
    private void validarTransicao(PedidoStatus atual, PedidoStatus novo) {
        boolean valida = switch (atual) {
            case AGUARDANDO_MOTORISTA -> novo == PedidoStatus.MOTORISTA_ENCONTRADO || novo == PedidoStatus.CANCELADO;
            case MOTORISTA_ENCONTRADO -> novo == PedidoStatus.EM_TRANSITO || novo == PedidoStatus.CANCELADO;
            case EM_TRANSITO -> novo == PedidoStatus.ENTREGUE || novo == PedidoStatus.CANCELADO;
            case ENTREGUE, CANCELADO -> false;
        };

        if (!valida) {
            throw new PedidoStatusInvalidoException(
                    "Transição de status inválida: " + atual + " → " + novo);
        }
    }
}
