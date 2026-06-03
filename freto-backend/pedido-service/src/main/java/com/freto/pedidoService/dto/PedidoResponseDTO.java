package com.freto.pedidoService.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.freto.pedidoService.model.enums.PedidoStatus;

public record PedidoResponseDTO(
    UUID id,
    UUID clienteId,
    UUID motoristaId,
    String origem,
    String destino,
    String descricao,
    PedidoStatus status,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {}
