import { useState } from "react";
import { Link } from "react-router-dom";
import { AuthLayout } from "../../components/AuthLayout";
import registerIllustration from "../../assets/illustration-register.svg";

type UserType = "cliente" | "motorista";

export default function RegisterPage() {
  const [userType, setUserType] = useState<UserType>("cliente");
  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: implementar cadastro
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
              Junte-se a milhares de usuários
            </h1>
            <p className="text-white/70 text-sm leading-relaxed">
              Crie sua conta e solicite seu primeiro frete em menos de 2
              minutos.
            </p>
            <img
              src={registerIllustration}
              alt="Ilustração de entregador"
              className="w-full max-w-[280px] max-h-[180px] object-contain mx-auto mt-4"
            />
          </div>

          <div className="flex gap-6 mt-6">
            {[
              "Motoristas verificados",
              "Rastreamento em tempo real",
              "Suporte 24h",
            ].map((f) => (
              <span key={f} className="text-white/80 text-xs font-medium">
                {f}
              </span>
            ))}
          </div>
        </>
      }
    >
      <div className="flex flex-col gap-5">
        <div>
          <h2 className="text-3xl font-bold text-gray-900">Crie sua conta</h2>
          <p className="text-gray-500 text-sm mt-1">
            Comece a usar o FRETO hoje mesmo
          </p>
        </div>

        {/* Tabs Cliente / Motorista */}
        <div className="grid grid-cols-2 border border-[#7C3AED] rounded-xl overflow-hidden">
          {(["cliente", "motorista"] as UserType[]).map((type) => (
            <button
              key={type}
              onClick={() => setUserType(type)}
              className={`py-2.5 text-sm font-medium capitalize transition ${
                userType === type
                  ? "bg-[#EDE9FE] text-[#7C3AED]"
                  : "bg-white text-gray-500 hover:bg-gray-50"
              }`}
            >
              {type.charAt(0).toUpperCase() + type.slice(1)}
            </button>
          ))}
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">
              Nome completo
            </label>
            <input
              type="text"
              name="name"
              placeholder="João Batista Cunha"
              value={form.name}
              onChange={handleChange}
              className="border border-gray-200 rounded-lg px-4 py-2 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">E-mail</label>
            <input
              type="email"
              name="email"
              placeholder="email@email.com"
              value={form.email}
              onChange={handleChange}
              className="border border-gray-200 rounded-lg px-4 py-2 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">
              Telefone
            </label>
            <input
              type="tel"
              name="phone"
              placeholder="(99) 9 9999 – 9999"
              value={form.phone}
              onChange={handleChange}
              className="border border-gray-200 rounded-lg px-4 py-2 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">Senha</label>
            <input
              type="password"
              name="password"
              placeholder="••••••••••••••"
              value={form.password}
              onChange={handleChange}
              className="border border-gray-200 rounded-lg px-4 py-2 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">
              Confirmar senha
            </label>
            <input
              type="password"
              name="confirmPassword"
              placeholder="••••••••••••••"
              value={form.confirmPassword}
              onChange={handleChange}
              className="border border-gray-200 rounded-lg px-4 py-2 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <button
            type="submit"
            className="w-full bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 rounded-full transition mt-1"
          >
            Criar conta grátis
          </button>
        </form>

        <p className="text-sm text-gray-500 text-center">
          Já tem conta?{" "}
          <Link
            to="/login"
            className="text-[#7C3AED] font-medium hover:underline"
          >
            Entrar
          </Link>
        </p>
      </div>
    </AuthLayout>
  );
}
