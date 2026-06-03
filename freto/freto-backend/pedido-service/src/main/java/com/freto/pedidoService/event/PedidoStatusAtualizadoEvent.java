package com.freto.pedidoService.event;

import java.util.UUID;

import com.freto.pedidoService.model.enums.PedidoStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoStatusAtualizadoEvent {
    private UUID pedidoId;
    private PedidoStatus novoStatus;
    private UUID motoristaId;
}
