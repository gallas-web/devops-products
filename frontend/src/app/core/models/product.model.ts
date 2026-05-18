export interface Category {
  id: number;
  name: string;
  description: string;
  icon: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Review {
  id: number;
  productId: number;
  userId: number;
  userName: string;
  rating: number;
  title: string;
  comment: string;
  verified: boolean;
  createdAt: string;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: Category;
  imageUrl: string;
  specifications: string;
  rating: number;
  reviewCount: number;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
  updatedAt: string;
}

export interface ProductDetail extends Product {
  reviews: Review[];
}

export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  quantity: number;
  categoryId: number;
  imageUrl: string;
  specifications: string;
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
  categoryId?: number;
  status?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: string;
}

export interface CreateReviewRequest {
  rating: number;
  title: string;
  comment?: string;
}

export const CATEGORIES = [
  'Informatique',
  'Téléphonie',
  'Audio',
  'TV & Vidéo',
  'Jeux Vidéo',
  'Accessoires'
];

export const STATUSES = [
  { label: 'Actif', value: 'ACTIVE' },
  { label: 'Inactif', value: 'INACTIVE' }
];
