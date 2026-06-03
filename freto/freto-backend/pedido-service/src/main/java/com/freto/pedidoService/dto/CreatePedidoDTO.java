package com.freto.pedidoService.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePedidoDTO(
    @NotNull UUID clienteId,
    @NotBlank String origem,
    @NotBlank String destino,
    String descricao
) {}
