export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: string;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
  updatedAt: string;
}

export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: string;
  status: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface ProductFilter {
  search?: string;
  category?: string;
  status?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: string;
}

export const CATEGORIES = [
  'Informatique',
  'Téléphonie',
  'Audio',
  'TV & Vidéo',
  'Électroménager',
  'Gaming',
  'Photo & Vidéo',
  'Accessoires'
];

export const STATUSES = [
  { label: 'Actif', value: 'ACTIVE' },
  { label: 'Inactif', value: 'INACTIVE' }
];
