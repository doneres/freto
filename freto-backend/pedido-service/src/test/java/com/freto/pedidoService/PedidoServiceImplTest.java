package com.freto.pedidoService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.freto.pedidoService.dto.AtualizarStatusDTO;
import com.freto.pedidoService.dto.CreatePedidoDTO;
import com.freto.pedidoService.dto.PedidoResponseDTO;
import com.freto.pedidoService.event.PedidoEventPublisher;
import com.freto.pedidoService.exception.PedidoNotFoundException;
import com.freto.pedidoService.exception.PedidoStatusInvalidoException;
import com.freto.pedidoService.model.Pedido;
import com.freto.pedidoService.model.enums.PedidoStatus;
import com.freto.pedidoService.repository.PedidoRepository;
import com.freto.pedidoService.service.impl.PedidoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoServiceImpl - Testes Unitários")
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoEventPublisher pedidoEventPublisher;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private UUID pedidoId;
    private UUID clienteId;
    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        pedidoId = UUID.randomUUID();
        clienteId = UUID.randomUUID();

        pedidoMock = new Pedido();
        pedidoMock.setId(pedidoId);
        pedidoMock.setClienteId(clienteId);
        pedidoMock.setOrigem("Rua das Flores, 10 - Goiânia");
        pedidoMock.setDestino("Av. Brasil, 500 - Anápolis");
        pedidoMock.setDescricao("Mudança residencial - 2 cômodos");
        pedidoMock.setStatus(PedidoStatus.AGUARDANDO_MOTORISTA);
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso e publicar evento na fila")
    void deveCriarPedidoComSucesso() {
        CreatePedidoDTO dto = new CreatePedidoDTO(
                clienteId, "Rua das Flores, 10", "Av. Brasil, 500", "Mudança pequena"
        );

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        PedidoResponseDTO response = pedidoService.criarPedido(dto);

        assertNotNull(response);
        assertEquals(clienteId, response.clienteId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(pedidoEventPublisher, times(1)).publishPedidoCriado(any());
    }

    @Test
    @DisplayName("Deve buscar pedido por ID com sucesso")
    void deveBuscarPedidoPorId() {
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoMock));

        PedidoResponseDTO response = pedidoService.buscarPorId(pedidoId);

        assertNotNull(response);
        assertEquals(pedidoId, response.id());
        assertEquals("Rua das Flores, 10 - Goiânia", response.origem());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não encontrado")
    void deveLancarExcecaoPedidoNaoEncontrado() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(PedidoNotFoundException.class, () -> pedidoService.buscarPorId(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve listar pedidos por cliente")
    void deveListarPedidosPorCliente() {
        when(pedidoRepository.findByClienteId(clienteId)).thenReturn(List.of(pedidoMock));

        List<PedidoResponseDTO> lista = pedidoService.listarPorCliente(clienteId);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals(clienteId, lista.get(0).clienteId());
    }

    @Test
    @DisplayName("Deve atualizar status de AGUARDANDO_MOTORISTA para MOTORISTA_ENCONTRADO")
    void deveAtualizarStatusComTransicaoValida() {
        UUID motoristaId = UUID.randomUUID();
        AtualizarStatusDTO dto = new AtualizarStatusDTO(PedidoStatus.MOTORISTA_ENCONTRADO, motoristaId);

        Pedido atualizado = new Pedido();
        atualizado.setId(pedidoId);
        atualizado.setClienteId(clienteId);
        atualizado.setStatus(PedidoStatus.MOTORISTA_ENCONTRADO);
        atualizado.setMotoristaId(motoristaId);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoMock));
        when(pedidoRepository.save(any())).thenReturn(atualizado);

        PedidoResponseDTO response = pedidoService.atualizarStatus(pedidoId, dto);

        assertEquals(PedidoStatus.MOTORISTA_ENCONTRADO, response.status());
        verify(pedidoEventPublisher, times(1)).publishStatusAtualizado(any());
    }

    @Test
    @DisplayName("Deve lançar exceção para transição de status inválida")
    void deveLancarExcecaoParaTransicaoInvalida() {
        // AGUARDANDO_MOTORISTA → ENTREGUE é inválido
        AtualizarStatusDTO dto = new AtualizarStatusDTO(PedidoStatus.ENTREGUE, null);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoMock));

        assertThrows(PedidoStatusInvalidoException.class,
                () -> pedidoService.atualizarStatus(pedidoId, dto));
    }

    @Test
    @DisplayName("Deve cancelar pedido com sucesso")
    void deveCancelarPedidoComSucesso() {
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoMock));
        when(pedidoRepository.save(any())).thenReturn(pedidoMock);

        assertDoesNotThrow(() -> pedidoService.cancelarPedido(pedidoId));
        verify(pedidoRepository, times(1)).save(any());
        verify(pedidoEventPublisher, times(1)).publishStatusAtualizado(any());
    }

    @Test
    @DisplayName("Não deve cancelar pedido já entregue")
    void naoDeveCancelarPedidoEntregue() {
        pedidoMock.setStatus(PedidoStatus.ENTREGUE);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoMock));

        assertThrows(PedidoStatusInvalidoException.class,
                () -> pedidoService.cancelarPedido(pedidoId));
    }

    @Test
    @DisplayName("Deve listar pedidos aguardando motorista")
    void deveListarPedidosAguardando() {
        when(pedidoRepository.findByStatus(PedidoStatus.AGUARDANDO_MOTORISTA))
                .thenReturn(List.of(pedidoMock));

        List<PedidoResponseDTO> lista = pedidoService.listarAguardando();

        assertEquals(1, lista.size());
        assertEquals(PedidoStatus.AGUARDANDO_MOTORISTA, lista.get(0).status());
    }
}
