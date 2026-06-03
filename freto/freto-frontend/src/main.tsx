import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import "./index.css";
import ForgotPasswordPage from "./pages/ForgotPassword/index.tsx";
import LoginPage from "./pages/Login/index.tsx";
import RegisterPage from "./pages/Register/index.tsx";
import SuccessPage from "./pages/Success/index.tsx";
import DashboardPage from "./pages/Dashboard/index.tsx";
import MotoristaDashboardPage from "./pages/MotoristaDashboard/index.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter basename={import.meta.env.BASE_URL}>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/success" element={<SuccessPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/motorista" element={<MotoristaDashboardPage />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
);
