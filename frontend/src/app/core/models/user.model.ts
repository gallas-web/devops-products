export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  address?: string;
  city?: string;
  zipCode?: string;
  country?: string;
  role: string;
  active: boolean;
  createdAt: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  user: User;
  expiresAt: string;
}

export interface UserUpdate {
  firstName: string;
  lastName: string;
  phone?: string;
  address?: string;
  city?: string;
  zipCode?: string;
  country?: string;
}
