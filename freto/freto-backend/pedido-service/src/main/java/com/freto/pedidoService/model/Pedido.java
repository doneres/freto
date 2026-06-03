package com.freto.pedidoService.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.freto.pedidoService.model.enums.PedidoStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedidos", schema = "pedido")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID clienteId;

    private UUID motoristaId;

    @Column(nullable = false)
    private String origem;

    @Column(nullable = false)
    private String destino;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.status = PedidoStatus.AGUARDANDO_MOTORISTA;
    }
}
