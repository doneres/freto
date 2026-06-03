import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { resetPassword } from "../../services/userService";

type Step = "form" | "success";

export default function ForgotPasswordPage() {
  const navigate = useNavigate();
  const [step, setStep] = useState<Step>("form");
  const [form, setForm] = useState({ email: "", newPassword: "", confirmNewPassword: "" });
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrorMessage("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (form.newPassword !== form.confirmNewPassword) {
      setErrorMessage("As senhas não coincidem.");
      return;
    }

    setLoading(true);
    try {
      await resetPassword({
        email: form.email,
        newPassword: form.newPassword,
        confirmNewPassword: form.confirmNewPassword,
      });
      setStep("success");
    } catch (error: any) {
      setErrorMessage(
        error.response?.data?.message || "Erro ao redefinir senha. Verifique o e-mail e tente novamente."
      );
    } finally {
      setLoading(false);
    }
  };

  if (step === "success") {
    return (
      <div className="h-screen bg-[#F9FAFB] flex items-center justify-center px-4">
        <div className="bg-white border border-gray-100 rounded-2xl shadow-sm p-10 w-full max-w-sm flex flex-col items-center gap-5">
          <span className="text-[#7C3AED] font-bold text-xl tracking-widest">FRETO</span>
          <div className="w-14 h-14 rounded-full bg-green-100 flex items-center justify-center">
            <svg width="28" height="28" viewBox="0 0 28 28" fill="none">
              <path d="M6 14L11 19L22 9" stroke="#22C55E" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </div>
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900">Senha redefinida!</h2>
            <p className="text-gray-500 text-sm mt-2">Sua senha foi atualizada com sucesso. Faça login com a nova senha.</p>
          </div>
          <button
            onClick={() => navigate("/login")}
            className="w-full bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 rounded-full transition"
          >
            Ir para o login
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="h-screen bg-[#F9FAFB] flex items-center justify-center px-4">
      <div className="bg-white border border-gray-100 rounded-2xl shadow-sm p-10 w-full max-w-sm flex flex-col items-center gap-5">
        <span className="text-[#7C3AED] font-bold text-xl tracking-widest">FRETO</span>

        <div className="w-14 h-14 rounded-full bg-[#EDE9FE] flex items-center justify-center">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M12 17v-2m0-4V7m-7 10a7 7 0 1 1 14 0" stroke="#7C3AED" strokeWidth="2" strokeLinecap="round" />
          </svg>
        </div>

        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900">Redefinir senha</h2>
          <p className="text-gray-500 text-sm mt-2 leading-relaxed">
            Informe seu e-mail e crie uma nova senha.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="w-full flex flex-col gap-4">
          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">E-mail</label>
            <input
              type="email"
              name="email"
              placeholder="seu@email.com"
              value={form.email}
              onChange={handleChange}
              required
              className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">Nova senha</label>
            <input
              type="password"
              name="newPassword"
              placeholder="••••••••"
              value={form.newPassword}
              onChange={handleChange}
              required
              className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
            <p className="text-xs text-gray-400">Mín. 8 caracteres, maiúscula, minúscula, número e símbolo (@#$%...)</p>
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-gray-700">Confirmar nova senha</label>
            <input
              type="password"
              name="confirmNewPassword"
              placeholder="••••••••"
              value={form.confirmNewPassword}
              onChange={handleChange}
              required
              className="border border-gray-200 rounded-lg px-4 py-2.5 text-sm placeholder:text-gray-400 outline-none focus:border-[#7C3AED] focus:ring-2 focus:ring-[#7C3AED]/10 transition"
            />
          </div>

          {errorMessage && (
            <p className="text-red-500 text-sm text-center">{errorMessage}</p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 rounded-full transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? "Redefinindo..." : "Redefinir senha"}
          </button>
        </form>

        <p className="text-sm text-gray-500">
          Lembrou a senha?{" "}
          <Link to="/login" className="text-[#7C3AED] font-medium hover:underline">
            Entrar
          </Link>
        </p>
      </div>
    </div>
  );
}
