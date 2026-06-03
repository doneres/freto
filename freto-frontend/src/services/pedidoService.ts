import axios from "axios";
import { CreatePedidoPayload, Pedido } from "../types/pedido";

const api = axios.create({
  baseURL: import.meta.env.VITE_PEDIDO_API_URL || "http://localhost:8082",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const criarPedido = async (payload: CreatePedidoPayload): Promise<Pedido> => {
  const { data } = await api.post<Pedido>("/api/pedidos", payload);
  return data;
};

export const listarPedidosPorCliente = async (clienteId: string): Promise<Pedido[]> => {
  const { data } = await api.get<Pedido[]>(`/api/pedidos?clienteId=${clienteId}`);
  return data;
};

export const listarPedidosAguardando = async (): Promise<Pedido[]> => {
  const { data } = await api.get<Pedido[]>("/api/pedidos");
  return data;
};

export const buscarPedido = async (id: string): Promise<Pedido> => {
  const { data } = await api.get<Pedido>(`/api/pedidos/${id}`);
  return data;
};

export const aceitarPedido = async (id: string, motoristaId: string): Promise<Pedido> => {
  const { data } = await api.put<Pedido>(`/api/pedidos/${id}/status`, {
    status: "MOTORISTA_ENCONTRADO",
    motoristaId,
  });
  return data;
};

export const atualizarStatus = async (id: string, status: string): Promise<Pedido> => {
  const { data } = await api.put<Pedido>(`/api/pedidos/${id}/status`, { status });
  return data;
};

export const cancelarPedido = async (id: string): Promise<void> => {
  await api.delete(`/api/pedidos/${id}`);
};
