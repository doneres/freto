import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { Pedido } from "../types/pedido";

const WS_URL = import.meta.env.VITE_WS_URL || "http://localhost:8082/ws";

/**
 * Hook que conecta ao WebSocket do pedido-service via STOMP/SockJS
 * e recebe atualizações de status em tempo real.
 */
export function usePedidoWebSocket(pedidoId: string | null) {
  const [pedidoAtualizado, setPedidoAtualizado] = useState<Pedido | null>(null);
  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    if (!pedidoId) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe(`/topic/pedidos/${pedidoId}`, (message) => {
          const pedido: Pedido = JSON.parse(message.body);
          setPedidoAtualizado(pedido);
        });
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
      client.deactivate();
    };
  }, [pedidoId]);

  return pedidoAtualizado;
}
