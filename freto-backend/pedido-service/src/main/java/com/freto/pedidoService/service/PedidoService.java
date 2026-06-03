package com.freto.pedidoService.service;

import java.util.List;
import java.util.UUID;

import com.freto.pedidoService.dto.AtualizarStatusDTO;
import com.freto.pedidoService.dto.CreatePedidoDTO;
import com.freto.pedidoService.dto.PedidoResponseDTO;

public interface PedidoService {
    PedidoResponseDTO criarPedido(CreatePedidoDTO dto);
    PedidoResponseDTO buscarPorId(UUID id);
    List<PedidoResponseDTO> listarPorCliente(UUID clienteId);
    List<PedidoResponseDTO> listarAguardando();
    PedidoResponseDTO atualizarStatus(UUID id, AtualizarStatusDTO dto);
    void cancelarPedido(UUID id);
}
