import { Link } from "react-router-dom";

export default function SuccessPage() {
  return (
    <div className="h-screen overflow-hidden bg-white flex items-center justify-center px-4">
      <div className="flex flex-col items-center gap-4 text-center max-w-sm">
        <div className="w-16 h-16 rounded-full bg-[#22C55E] flex items-center justify-center">
          <svg width="30" height="30" viewBox="0 0 30 30" fill="none">
            <path
              d="M7 15L12 20L23 10"
              stroke="white"
              strokeWidth="2.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </div>

        <h2 className="text-2xl font-bold text-gray-900">
          Conta criada com sucesso!
        </h2>

        <p className="text-gray-500 text-sm leading-relaxed">
          Bem-vindo ao FRETO! Sua conta foi criada e já está pronta para uso.
        </p>

        <Link
          to="/dashboard"
          className="mt-2 bg-[#7C3AED] hover:bg-[#5B21B6] text-white font-semibold py-3 px-10 rounded-full transition"
        >
          Ir para o Dashboard
        </Link>
      </div>
    </div>
  );
}
