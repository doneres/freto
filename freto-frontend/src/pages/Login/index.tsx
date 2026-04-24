import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthLayout } from "../../components/AuthLayout";
import loginIllustration from "../../assets/illustration-login.svg";
import { loginUser } from "../../services/userService";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await loginUser({ email, password });
      localStorage.setItem("token", response.token); // armazena o token
      navigate("/dashboard");
    } catch (error: any) {
      setErrorMessage(
        error.response?.data?.message ||
          "Erro ao fazer login. Tente novamente.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout
      sidebar={
        <>
          <span className="text-white font-bold text-xl tracking-widest">
            FRETO
          </span>

          <div className="flex flex-col gap-4 flex-1 justify-center">
            <h1 className="text-white font-bold text-4xl leading-tight">
              Frete rápido,
              <br />
              sem stress,
              <br />
              sem surpresa.
            </h1>
            <p className="text-white/70 text-sm leading-relaxed max-w-[240px]">
              Conectamos você ao motorista ideal em segundos. Acompanhe tudo em
              tempo real.
            </p>
            <img
              src={loginIllustration}
              alt="Ilustração de um caminhão de frete"
              className="w-full max-w-[300px] mx-auto mt-4"
            />
          </div>

          <div className="flex gap-8 mt-6">
            <div>
              <p className="text-white font-bold text-2xl">98%</p>
              <p className="text-white/60 text-xs mt-1">Satisfação</p>
            </div>
            <div>
              <p className="text-white font-bold text-2xl">+2k</p>
              <p className="text-white/60 text-xs mt-1">Motoristas</p>
            </div>
            <div>
              <p className="text-white font-bold text-2xl">&lt;2min</p>
              <p className="text-white/60 text-xs mt-1">Matching</p>
            </div>
          </div>
        </>
      }
    >
      <div className="flex flex-col gap-6">
        <div>
          <h2 className="text-3xl font-bold text-gray-900">
            Bem-vindo de volta
          </h2>
          <p className="text-gray-500 text-sm mt-1">
            Entre para acompanhar seus fretes
          </p>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">E-mail</label>
            <input
              type="email"
              placeholder="seu@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm text-gray-500 placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">Senha</label>
            <input
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm text-gray-500 placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
            <Link
              to="/forgot-password"
              className="text-[#7C3AED] text-sm text-right hover:underline"
            >
              Esqueceu a senha?
            </Link>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 rounded-full transition mt-2 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? "Entrando..." : "Entrar na conta"}
          </button>

          {errorMessage && (
            <p className="text-red-500 text-sm text-center">{errorMessage}</p>
          )}
        </form>

        <p className="text-sm text-gray-500 text-center">
          Não tem conta?{" "}
          <Link
            to="/register"
            className="text-[#7C3AED] font-medium hover:underline"
          >
            Cadastre-se grátis
          </Link>
        </p>
      </div>
    </AuthLayout>
  );
}
