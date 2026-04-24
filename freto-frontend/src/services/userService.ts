import api from "./api";

export interface CreateUserData {
  name: string;
  email: string;
  phoneNumber: string;
  password: string;
  confirmPassword: string;
  role: "CONTRATANTE" | "TRANSPORTADOR";
}

export async function createUser(data: CreateUserData) {
  const response = await api.post("/api/users", data);
  return response.data;
}

export interface LoginData {
  email: string;
  password: string;
}

export async function loginUser(data: LoginData) {
  const response = await api.post("/api/auth/login", data);
  return response.data;
}
