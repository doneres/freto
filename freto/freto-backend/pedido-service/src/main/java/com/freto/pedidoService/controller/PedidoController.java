package com.freto.pedidoService.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.freto.pedidoService.dto.AtualizarStatusDTO;
import com.freto.pedidoService.dto.CreatePedidoDTO;
import com.freto.pedidoService.dto.PedidoResponseDTO;
import com.freto.pedidoService.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody CreatePedidoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar(@RequestParam(required = false) UUID clienteId) {
        if (clienteId != null) {
            return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
        }
        return ResponseEntity.ok(pedidoService.listarAguardando());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarStatusDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
