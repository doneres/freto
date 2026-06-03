package com.freto.pedidoService.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freto.pedidoService.model.Pedido;
import com.freto.pedidoService.model.enums.PedidoStatus;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    List<Pedido> findByClienteId(UUID clienteId);
    List<Pedido> findByMotoristaId(UUID motoristaId);
    List<Pedido> findByStatus(PedidoStatus status);
}
