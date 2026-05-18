/* eslint-disable @angular-eslint/prefer-inject */
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzRateModule } from 'ng-zorro-antd/rate';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzStatisticModule } from 'ng-zorro-antd/statistic';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { Cart } from '../../../core/models/cart.model';
import { CreateOrderRequest, Order } from '../../../core/models/order.model';
import { Category, PageResponse, Product, ProductFilter } from '../../../core/models/product.model';
import { LoginRequest, RegisterRequest, User } from '../../../core/models/user.model';
import { AuthService } from '../../../core/services/auth.service';
import { CartService } from '../../../core/services/cart.service';
import { OrderService } from '../../../core/services/order.service';
import { ProductService } from '../../../core/services/product.service';

@Component({
  selector: 'app-shop-page',
  standalone: true,
  imports: [
    CommonModule, FormsModule, RouterLink,
    NzBadgeModule, NzButtonModule, NzCardModule, NzDividerModule, NzEmptyModule,
    NzFormModule, NzGridModule, NzIconModule, NzInputModule, NzInputNumberModule,
    NzModalModule, NzRateModule, NzSelectModule, NzSpinModule, NzStatisticModule,
    NzTableModule, NzTabsModule, NzTagModule
  ],
  templateUrl: './shop-page.component.html',
  styleUrls: ['./shop-page.component.scss']
})
export class ShopPageComponent implements OnInit {
  user: User | null = null;
  products: PageResponse<Product> = { content: [], pageNumber: 0, pageSize: 12, totalElements: 0, totalPages: 0, last: true };
  categories: Category[] = [];
  cart: Cart | null = null;
  orders: Order[] = [];
  selectedTab = 0;
  loadingProducts = false;
  loadingCart = false;
  authVisible = false;
  authMode: 'login' | 'register' = 'login';

  filter: ProductFilter = { page: 0, size: 12, sortBy: 'createdAt', sortDir: 'desc', status: 'ACTIVE' };
  loginForm: LoginRequest = { email: '', password: '' };
  registerForm: RegisterRequest = { email: '', password: '', confirmPassword: '', firstName: '', lastName: '' };
  checkout: CreateOrderRequest = {
    shippingAddress: '',
    shippingCity: '',
    shippingZipCode: '',
    shippingCountry: 'France',
    paymentMethod: 'CARD'
  };

  constructor(
    private authService: AuthService,
    private productService: ProductService,
    private cartService: CartService,
    private orderService: OrderService,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.user = user;
      if (user) {
        this.loadCart();
        this.loadOrders();
      } else {
        this.cart = null;
        this.orders = [];
      }
    });
    this.loadCategories();
    this.loadProducts();
  }

  loadProducts(): void {
    this.loadingProducts = true;
    this.productService.getAll(this.filter).subscribe({
      next: data => {
        this.products = data;
        this.loadingProducts = false;
      },
      error: () => this.loadingProducts = false
    });
  }

  loadCategories(): void {
    this.productService.getCategories().subscribe({
      next: categories => this.categories = categories
    });
  }

  loadCart(): void {
    this.loadingCart = true;
    this.cartService.getCart().subscribe({
      next: cart => {
        this.cart = cart;
        this.loadingCart = false;
      },
      error: () => this.loadingCart = false
    });
  }

  loadOrders(): void {
    this.orderService.getMyOrders({ size: 20 }).subscribe({
      next: page => this.orders = page.content
    });
  }

  onSearch(value: string): void {
    this.filter.search = value || undefined;
    this.filter.page = 0;
    this.loadProducts();
  }

  onCategoryChange(categoryId?: number): void {
    this.filter.categoryId = categoryId;
    this.filter.page = 0;
    this.loadProducts();
  }

  selectCategory(categoryId?: number): void {
    this.onCategoryChange(categoryId);
  }

  nextPage(): void {
    if (this.products.last) return;
    this.filter.page = (this.filter.page ?? 0) + 1;
    this.loadProducts();
  }

  previousPage(): void {
    if ((this.filter.page ?? 0) === 0) return;
    this.filter.page = (this.filter.page ?? 0) - 1;
    this.loadProducts();
  }

  addProduct(product: Product): void {
    if (!this.user) {
      this.openAuth('login');
      return;
    }
    this.cartService.addToCart({ productId: product.id, quantity: 1 }).subscribe({
      next: cart => {
        this.cart = cart;
        this.message.success('Produit ajouté au panier');
      }
    });
  }

  updateQuantity(cartItemId: number, quantity: number): void {
    this.cartService.updateItem({ cartItemId, quantity }).subscribe({
      next: cart => this.cart = cart
    });
  }

  removeItem(cartItemId: number): void {
    this.cartService.removeItem(cartItemId).subscribe({
      next: () => this.loadCart()
    });
  }

  createOrder(): void {
    if (!this.cart?.items.length) return;
    this.orderService.create(this.checkout).subscribe({
      next: order => {
        this.message.success(`Commande ${order.orderNumber} créée`);
        this.selectedTab = 2;
        this.loadCart();
        this.loadOrders();
      }
    });
  }

  openAuth(mode: 'login' | 'register'): void {
    this.authMode = mode;
    this.authVisible = true;
  }

  submitAuth(): void {
    const request = this.authMode === 'login'
      ? this.authService.login(this.loginForm)
      : this.authService.register(this.registerForm);

    request.subscribe({
      next: () => {
        this.authVisible = false;
        this.message.success(this.authMode === 'login' ? 'Connexion réussie' : 'Compte créé');
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.selectedTab = 0;
  }

  get cartCount(): number {
    return this.cart?.items.reduce((sum, item) => sum + item.quantity, 0) ?? 0;
  }

  get activeCategoryName(): string {
    return this.categories.find(category => category.id === this.filter.categoryId)?.name ?? 'Toutes les collections';
  }

  get checkoutReady(): boolean {
    return !!this.cart?.items.length
      && !!this.checkout.shippingAddress
      && !!this.checkout.shippingCity
      && !!this.checkout.shippingZipCode
      && !!this.checkout.shippingCountry;
  }

  getStockLabel(product: Product): string {
    if (product.quantity === 0) return 'Rupture';
    if (product.quantity < 10) return 'Stock faible';
    return 'En stock';
  }

  getCategoryIcon(category: Category): string {
    const icon = category.icon || '';
    if (icon.includes('phone')) return 'mobile';
    if (icon.includes('audio') || icon.includes('head')) return 'customer-service';
    if (icon.includes('video')) return 'play-square';
    if (icon.includes('game')) return 'thunderbolt';
    if (icon.includes('plug')) return 'usb';
    return 'appstore';
  }

  getOrderStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      PENDING: 'En attente',
      CONFIRMED: 'Confirmée',
      SHIPPED: 'Expédiée',
      DELIVERED: 'Livrée',
      CANCELLED: 'Annulée',
      REFUNDED: 'Remboursée'
    };
    return labels[status] ?? status;
  }
}
