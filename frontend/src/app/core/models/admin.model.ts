import { OrderItem } from './order.model';

export interface AdminDashboardStatsDto {
  totalUsers: number;
  totalOrders: number;
  totalRevenue: number;
  totalProducts: number;
  averageOrderValue: number;
  totalReviews: number;
  lowStockProducts: number;
  lastUpdated: string;
}

export interface AdminOrderDto {
  id: number;
  orderNumber: string;
  userId: number;
  userName: string;
  userEmail: string;
  totalPrice: number;
  status: string;
  paymentStatus: string;
  shippingAddress?: string;
  shippingCity?: string;
  shippingZipCode?: string;
  shippingCountry?: string;
  items: OrderItem[];
  shippingCost: number;
  tax: number;
  trackingNumber?: string;
  createdAt: string;
}

export interface AdminUserDto {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  enabled: boolean;
  role: string;
  orderCount: number;
  totalSpent: number;
  createdAt: string;
  phone?: string;
  address?: string;
  city?: string;
  zipCode?: string;
  country?: string;
}
