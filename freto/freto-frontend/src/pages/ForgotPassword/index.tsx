import { useState } from "react";
import { Link } from "react-router-dom";

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: implementar envio de e-mail de redefinição
  };

  return (
    <div className="h-screen overflow-hidden bg-[#F9FAFB] flex items-center justify-center px-4">
      <div className="bg-white border border-gray-100 rounded-2xl shadow-sm p-10 w-full max-w-sm flex flex-col items-center gap-5">
        <span className="text-[#7C3AED] font-bold text-xl tracking-widest">
          FRETO
        </span>

        <div className="w-14 h-14 rounded-full bg-[#EDE9FE] flex items-center justify-center text-2xl">
          🔒
        </div>

        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900">
            Redefinir sua senha
          </h2>
          <p className="text-gray-500 text-sm mt-2 leading-relaxed">
            Digite seu e-mail e enviaremos um link de redefinição
          </p>
        </div>

        <form onSubmit={handleSubmit} className="w-full flex flex-col gap-4">
          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">E-mail</label>
            <input
              type="email"
              placeholder="email@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <button
            type="submit"
            className="self-center bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 px-8 rounded-full transition"
          >
            Enviar link de redefinição
          </button>
        </form>

        <p className="text-sm text-gray-500">
          Lembrou a senha?{" "}
          <Link
            to="/login"
            className="text-[#7C3AED] font-medium hover:underline"
          >
            Entrar
          </Link>
        </p>
      </div>
    </div>
  );
}
