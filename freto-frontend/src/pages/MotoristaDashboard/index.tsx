import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  listarPedidosAguardando,
  aceitarPedido,
  atualizarStatus,
} from "../../services/pedidoService";
import { Pedido } from "../../types/pedido";

const PROXIMOS_STATUS: Record<string, string | null> = {
  MOTORISTA_ENCONTRADO: "EM_TRANSITO",
  EM_TRANSITO: "ENTREGUE",
  ENTREGUE: null,
};

const BOTAO_LABEL: Record<string, string> = {
  MOTORISTA_ENCONTRADO: "Iniciar transporte",
  EM_TRANSITO: "Confirmar entrega",
};

export default function MotoristaDashboardPage() {
  const navigate = useNavigate();
  const [pedidosDisponiveis, setPedidosDisponiveis] = useState<Pedido[]>([]);
  const [pedidosAtivos, setPedidosAtivos] = useState<Pedido[]>([]);
  const [erro, setErro] = useState("");

  const motoristaId = localStorage.getItem("clienteId") || "";

  useEffect(() => {
    if (!motoristaId) { navigate("/login"); return; }
    carregarDisponivel();
    const interval = setInterval(carregarDisponivel, 15000);
    return () => clearInterval(interval);
  }, []);

  const carregarDisponivel = async () => {
    try {
      const lista = await listarPedidosAguardando();
      setPedidosDisponiveis(lista);
    } catch {
      // silencia
    }
  };

  const handleAceitar = async (pedido: Pedido) => {
    try {
      const atualizado = await aceitarPedido(pedido.id, motoristaId);
      setPedidosDisponiveis((prev) => prev.filter((p) => p.id !== pedido.id));
      setPedidosAtivos((prev) => [atualizado, ...prev]);
    } catch {
      setErro("Erro ao aceitar pedido.");
    }
  };

  const handleAvancarStatus = async (pedido: Pedido) => {
    const proximo = PROXIMOS_STATUS[pedido.status];
    if (!proximo) return;
    try {
      const atualizado = await atualizarStatus(pedido.id, proximo);
      setPedidosAtivos((prev) =>
        prev.map((p) => (p.id === atualizado.id ? atualizado : p))
      );
    } catch {
      setErro("Erro ao atualizar status.");
    }
  };

  const logout = () => { localStorage.clear(); navigate("/login"); };

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
        <span className="text-[#7C3AED] font-bold text-xl tracking-widest">FRETO</span>
        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-500">Dashboard do Motorista</span>
          <button onClick={logout} className="text-sm text-gray-400 hover:text-red-500 transition">
            Sair
          </button>
        </div>
      </header>

      <main className="max-w-3xl mx-auto px-4 py-8 flex flex-col gap-8">
        {erro && <p className="text-red-500 text-sm">{erro}</p>}

        {/* Pedidos em andamento */}
        {pedidosAtivos.length > 0 && (
          <section className="flex flex-col gap-4">
            <h2 className="text-lg font-bold text-gray-800">Em andamento</h2>
            {pedidosAtivos.map((pedido) => (
              <div key={pedido.id} className="bg-white rounded-2xl border border-[#7C3AED]/30 shadow-sm p-5 flex flex-col gap-3">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-semibold px-3 py-1 rounded-full bg-purple-100 text-purple-700">
                    {pedido.status.replace(/_/g, " ")}
                  </span>
                </div>
                <div className="flex flex-col gap-1 text-sm text-gray-600">
                  <div className="flex gap-2"><span className="text-[#7C3AED] font-bold">↑</span>{pedido.origem}</div>
                  <div className="flex gap-2"><span className="text-green-500 font-bold">↓</span>{pedido.destino}</div>
                  {pedido.descricao && <p className="text-xs text-gray-400 italic">{pedido.descricao}</p>}
                </div>
                {BOTAO_LABEL[pedido.status] && (
                  <button
                    onClick={() => handleAvancarStatus(pedido)}
                    className="w-full bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 rounded-full transition"
                  >
                    {BOTAO_LABEL[pedido.status]}
                  </button>
                )}
                {pedido.status === "ENTREGUE" && (
                  <p className="text-center text-green-600 text-sm font-semibold">Entrega concluída ✓</p>
                )}
              </div>
            ))}
          </section>
        )}

        {/* Pedidos disponíveis */}
        <section className="flex flex-col gap-4">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-bold text-gray-800">Pedidos disponíveis</h2>
            <button
              onClick={carregarDisponivel}
              className="text-xs text-[#7C3AED] hover:underline"
            >
              Atualizar
            </button>
          </div>

          {pedidosDisponiveis.length === 0 && (
            <p className="text-sm text-gray-400 text-center py-8">
              Nenhum pedido disponível no momento.
            </p>
          )}

          {pedidosDisponiveis.map((pedido) => (
            <div key={pedido.id} className="bg-white rounded-2xl border border-gray-100 shadow-sm p-5 flex flex-col gap-3">
              <div className="flex flex-col gap-1 text-sm text-gray-600">
                <div className="flex gap-2"><span className="text-[#7C3AED] font-bold">↑</span>{pedido.origem}</div>
                <div className="flex gap-2"><span className="text-green-500 font-bold">↓</span>{pedido.destino}</div>
                {pedido.descricao && <p className="text-xs text-gray-400 italic">{pedido.descricao}</p>}
              </div>
              <button
                onClick={() => handleAceitar(pedido)}
                className="w-full border-2 border-[#7C3AED] text-[#7C3AED] hover:bg-[#7C3AED] hover:text-white font-semibold py-3 rounded-full transition"
              >
                Aceitar pedido
              </button>
            </div>
          ))}
        </section>
      </main>
    </div>
  );
}
