import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  criarPedido,
  listarPedidosPorCliente,
  cancelarPedido,
} from "../../services/pedidoService";
import { Pedido } from "../../types/pedido";
import { usePedidoWebSocket } from "../../hooks/usePedidoWebSocket";

const STATUS_LABEL: Record<string, string> = {
  AGUARDANDO_MOTORISTA: "Aguardando motorista...",
  MOTORISTA_ENCONTRADO: "Motorista a caminho!",
  EM_TRANSITO: "Em trânsito",
  ENTREGUE: "Entregue ✓",
  CANCELADO: "Cancelado",
};

const STATUS_COLOR: Record<string, string> = {
  AGUARDANDO_MOTORISTA: "bg-yellow-100 text-yellow-700",
  MOTORISTA_ENCONTRADO: "bg-blue-100 text-blue-700",
  EM_TRANSITO: "bg-purple-100 text-purple-700",
  ENTREGUE: "bg-green-100 text-green-700",
  CANCELADO: "bg-red-100 text-red-600",
};

export default function DashboardPage() {
  const navigate = useNavigate();
  const [pedidos, setPedidos] = useState<Pedido[]>([]);
  const [pedidoAtivoId, setPedidoAtivoId] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [form, setForm] = useState({ origem: "", destino: "", descricao: "" });
  const [mostrarForm, setMostrarForm] = useState(false);
  const [erro, setErro] = useState("");

  // Simula ID do cliente logado (em produção: extrair do JWT)
  const clienteId = localStorage.getItem("clienteId") || "";

  // WebSocket: escuta atualizações do pedido ativo
  const pedidoAtualizado = usePedidoWebSocket(pedidoAtivoId);

  useEffect(() => {
    if (pedidoAtualizado) {
      setPedidos((prev) =>
        prev.map((p) => (p.id === pedidoAtualizado.id ? pedidoAtualizado : p))
      );
    }
  }, [pedidoAtualizado]);

  useEffect(() => {
    if (!clienteId) {
      navigate("/login");
      return;
    }
    carregarPedidos();
  }, [clienteId]);

  const carregarPedidos = async () => {
    try {
      const lista = await listarPedidosPorCliente(clienteId);
      setPedidos(lista);
      // Monitora o pedido mais recente ainda ativo
      const ativo = lista.find(
        (p) => p.status !== "ENTREGUE" && p.status !== "CANCELADO"
      );
      if (ativo) setPedidoAtivoId(ativo.id);
    } catch {
      // silencia erro de lista vazia
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.origem || !form.destino) {
      setErro("Origem e destino são obrigatórios.");
      return;
    }
    setLoading(true);
    setErro("");
    try {
      const novo = await criarPedido({ clienteId, ...form });
      setPedidos((prev) => [novo, ...prev]);
      setPedidoAtivoId(novo.id);
      setForm({ origem: "", destino: "", descricao: "" });
      setMostrarForm(false);
    } catch {
      setErro("Erro ao criar pedido. Tente novamente.");
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = async (id: string) => {
    try {
      await cancelarPedido(id);
      setPedidos((prev) =>
        prev.map((p) => (p.id === id ? { ...p, status: "CANCELADO" } : p))
      );
    } catch {
      setErro("Não foi possível cancelar o pedido.");
    }
  };

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
        <span className="text-[#7C3AED] font-bold text-xl tracking-widest">FRETO</span>
        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-500">Dashboard do Cliente</span>
          <button
            onClick={logout}
            className="text-sm text-gray-400 hover:text-red-500 transition"
          >
            Sair
          </button>
        </div>
      </header>

      <main className="max-w-3xl mx-auto px-4 py-8 flex flex-col gap-6">
        {/* CTA novo pedido */}
        {!mostrarForm && (
          <button
            onClick={() => setMostrarForm(true)}
            className="w-full bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-4 rounded-2xl transition shadow-md text-lg"
          >
            + Solicitar novo frete
          </button>
        )}

        {/* Formulário */}
        {mostrarForm && (
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h2 className="text-xl font-bold text-gray-900 mb-4">Novo pedido de frete</h2>
            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-gray-700">Origem</label>
                <input
                  type="text"
                  placeholder="Rua, número, bairro, cidade"
                  value={form.origem}
                  onChange={(e) => setForm({ ...form, origem: e.target.value })}
                  className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
                />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-gray-700">Destino</label>
                <input
                  type="text"
                  placeholder="Rua, número, bairro, cidade"
                  value={form.destino}
                  onChange={(e) => setForm({ ...form, destino: e.target.value })}
                  className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
                />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-gray-700">Descrição (opcional)</label>
                <input
                  type="text"
                  placeholder="Ex: Mudança de 2 cômodos, itens frágeis..."
                  value={form.descricao}
                  onChange={(e) => setForm({ ...form, descricao: e.target.value })}
                  className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
                />
              </div>
              {erro && <p className="text-red-500 text-sm">{erro}</p>}
              <div className="flex gap-3">
                <button
                  type="submit"
                  disabled={loading}
                  className="flex-1 bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 rounded-full transition disabled:opacity-50"
                >
                  {loading ? "Solicitando..." : "Confirmar pedido"}
                </button>
                <button
                  type="button"
                  onClick={() => setMostrarForm(false)}
                  className="px-6 py-3 border border-gray-200 rounded-full text-gray-500 hover:bg-gray-50 transition text-sm font-medium"
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Lista de pedidos */}
        <div className="flex flex-col gap-4">
          <h2 className="text-lg font-bold text-gray-800">Meus fretes</h2>

          {pedidos.length === 0 && (
            <p className="text-sm text-gray-400 text-center py-8">
              Nenhum frete solicitado ainda.
            </p>
          )}

          {pedidos.map((pedido) => (
            <div
              key={pedido.id}
              className="bg-white rounded-2xl border border-gray-100 shadow-sm p-5 flex flex-col gap-3"
            >
              <div className="flex items-center justify-between">
                <span className={`text-xs font-semibold px-3 py-1 rounded-full ${STATUS_COLOR[pedido.status]}`}>
                  {STATUS_LABEL[pedido.status]}
                </span>
                <span className="text-xs text-gray-400">
                  {new Date(pedido.criadoEm).toLocaleDateString("pt-BR")}
                </span>
              </div>

              <div className="flex flex-col gap-1">
                <div className="flex items-center gap-2 text-sm text-gray-600">
                  <span className="text-[#7C3AED] font-bold">↑</span>
                  <span>{pedido.origem}</span>
                </div>
                <div className="flex items-center gap-2 text-sm text-gray-600">
                  <span className="text-green-500 font-bold">↓</span>
                  <span>{pedido.destino}</span>
                </div>
                {pedido.descricao && (
                  <p className="text-xs text-gray-400 mt-1 italic">{pedido.descricao}</p>
                )}
              </div>

              {/* Barra de progresso de status */}
              <StatusProgress status={pedido.status} />

              {pedido.status === "AGUARDANDO_MOTORISTA" && (
                <button
                  onClick={() => handleCancelar(pedido.id)}
                  className="text-xs text-red-400 hover:text-red-600 text-right transition"
                >
                  Cancelar pedido
                </button>
              )}
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}

function StatusProgress({ status }: { status: string }) {
  const steps = ["AGUARDANDO_MOTORISTA", "MOTORISTA_ENCONTRADO", "EM_TRANSITO", "ENTREGUE"];
  const currentIndex = steps.indexOf(status);

  if (status === "CANCELADO") {
    return (
      <p className="text-xs text-red-400 font-medium">Pedido cancelado</p>
    );
  }

  return (
    <div className="flex items-center gap-1 mt-1">
      {steps.map((step, i) => (
        <div key={step} className="flex items-center gap-1 flex-1">
          <div
            className={`h-1.5 rounded-full flex-1 transition-all ${
              i <= currentIndex ? "bg-[#7C3AED]" : "bg-gray-200"
            }`}
          />
        </div>
      ))}
    </div>
  );
}
