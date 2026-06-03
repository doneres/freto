package com.freto.pedidoService.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoCriadoEvent {
    private UUID pedidoId;
    private UUID clienteId;
    private String origem;
    private String destino;
    private String descricao;
}
