export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  items: OrderItem[];
  totalPrice: number;
  shippingCost: number;
  tax: number;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'REFUNDED';
  paymentStatus: 'PENDING' | 'PAID' | 'FAILED' | 'REFUNDED';
  shippingAddress: string;
  shippingCity: string;
  shippingZipCode: string;
  shippingCountry: string;
  trackingNumber?: string;
  createdAt: string;
  shippedAt?: string;
  deliveredAt?: string;
}

export interface CreateOrderRequest {
  shippingAddress: string;
  shippingCity: string;
  shippingZipCode: string;
  shippingCountry: string;
  paymentMethod: string;
}

export interface OrderFilter {
  status?: string;
  paymentStatus?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: string;
}

export const ORDER_STATUSES = [
  { label: 'En attente', value: 'PENDING' },
  { label: 'Confirmée', value: 'CONFIRMED' },
  { label: 'Expédiée', value: 'SHIPPED' },
  { label: 'Livrée', value: 'DELIVERED' },
  { label: 'Annulée', value: 'CANCELLED' },
  { label: 'Remboursée', value: 'REFUNDED' }
];

export const PAYMENT_STATUSES = [
  { label: 'En attente', value: 'PENDING' },
  { label: 'Payée', value: 'PAID' },
  { label: 'Échouée', value: 'FAILED' },
  { label: 'Remboursée', value: 'REFUNDED' }
];
