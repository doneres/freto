package com.freto.pedidoService.dto;

import java.util.UUID;

import com.freto.pedidoService.model.enums.PedidoStatus;

import jakarta.validation.constraints.NotNull;

public record AtualizarStatusDTO(
    @NotNull PedidoStatus status,
    UUID motoristaId
) {}
