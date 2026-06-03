export type PedidoStatus =
  | "AGUARDANDO_MOTORISTA"
  | "MOTORISTA_ENCONTRADO"
  | "EM_TRANSITO"
  | "ENTREGUE"
  | "CANCELADO";

export interface Pedido {
  id: string;
  clienteId: string;
  motoristaId: string | null;
  origem: string;
  destino: string;
  descricao: string;
  status: PedidoStatus;
  criadoEm: string;
  atualizadoEm: string;
}

export interface CreatePedidoPayload {
  clienteId: string;
  origem: string;
  destino: string;
  descricao?: string;
}
