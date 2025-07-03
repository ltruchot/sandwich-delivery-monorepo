const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export interface AuthResponse {
  token: string;
  email: string;
}

export interface Topping {
  id: number;
  name: string;
  price: number;
}

export interface SandwichRequest {
  type: 'HAM' | 'VEGETARIAN';
  toppingIds: number[];
}

export interface Sandwich {
  id: number;
  type: 'HAM' | 'VEGETARIAN';
  toppingIds: number[];
  totalPrice: number;
}

export interface Order {
  id: number;
  sandwiches: Sandwich[];
  status: 'BASKET' | 'PAID' | 'DELIVERED';
  createdAt: string;
  paidAt?: string;
  totalPrice: number;
}

class ApiClient {
  private getAuthHeaders(): HeadersInit {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      ...(token && { Authorization: `Bearer ${token}` }),
    };
  }

  async register(email: string, password: string): Promise<AuthResponse> {
    const response = await fetch(`${API_BASE}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    if (!response.ok) throw new Error('Registration failed');
    return response.json();
  }

  async login(email: string, password: string): Promise<AuthResponse> {
    const response = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    if (!response.ok) throw new Error('Login failed');
    return response.json();
  }

  async getToppings(): Promise<Topping[]> {
    const response = await fetch(`${API_BASE}/toppings`);
    if (!response.ok) throw new Error('Failed to fetch toppings');
    return response.json();
  }

  async getCurrentBasket(): Promise<Order> {
    const response = await fetch(`${API_BASE}/orders/basket`, {
      headers: this.getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Failed to fetch basket');
    return response.json();
  }

  async addSandwichToBasket(sandwich: SandwichRequest): Promise<Order> {
    const response = await fetch(`${API_BASE}/orders/basket/sandwich`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(sandwich),
    });
    if (!response.ok) throw new Error('Failed to add sandwich to basket');
    return response.json();
  }

  async payOrder(): Promise<Order> {
    const response = await fetch(`${API_BASE}/orders/pay`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Payment failed');
    return response.json();
  }

  async getUserOrders(): Promise<Order[]> {
    const response = await fetch(`${API_BASE}/orders`, {
      headers: this.getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Failed to fetch orders');
    return response.json();
  }
}

export const api = new ApiClient();