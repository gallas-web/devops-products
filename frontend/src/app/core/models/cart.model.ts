export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
  totalPrice: number;
  imageUrl: string;
  addedAt: string;
}

export interface Cart {
  id: number;
  items: CartItem[];
  totalPrice: number;
  itemCount: number;
  updatedAt: string;
}

export interface AddToCartRequest {
  productId: number;
  quantity: number;
}

export interface UpdateCartItemRequest {
  cartItemId: number;
  quantity: number;
}
