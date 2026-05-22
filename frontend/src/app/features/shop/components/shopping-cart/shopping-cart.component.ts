import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../../../core/services/cart.service';
import { Cart, CartItem } from '../../../../core/models/cart.model';

import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../../../core/services/order.service';
import { CreateOrderRequest, Order } from '../../../../core/models/order.model';


@Component({
  selector: 'app-shopping-cart',
  standalone: true,
  imports: [
    CommonModule,
    NzGridModule,
    NzCardModule,
    NzButtonModule,
    NzInputNumberModule,
    NzToolTipModule,
    NzEmptyModule,
    NzSpinModule,
    NzDividerModule,
    NzBadgeModule,
    NzIconModule,
    NzTableModule,
    NzSelectModule,
    FormsModule
  ],

  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit {
  cart: Cart | null = null;
  loading = true;
  checkout: CreateOrderRequest = {
    shippingAddress: '',
    shippingCity: '',
    shippingZipCode: '',
    shippingCountry: 'France',
    paymentMethod: 'CARD'
  };

  get checkoutReady(): boolean {

    return (
      !!this.checkout.shippingAddress?.trim() &&
      !!this.checkout.shippingCity?.trim() &&
      !!this.checkout.shippingZipCode?.trim() &&
      !!this.checkout.shippingCountry?.trim() &&
      !!this.checkout.paymentMethod
    );
  }

  constructor(
    private cartService: CartService,
    private orderService: OrderService
  ) {}


  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.loading = true;
    this.cartService.getCart().subscribe({
      next: (cart: Cart) => {
        this.cart = cart;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  updateQuantity(cartItemId: number, quantity: number): void {
    if (quantity <= 0) {
      this.removeItem(cartItemId);
      return;
    }
    this.cartService.updateItem({ cartItemId, quantity }).subscribe({
      next: (cart: Cart) => {
        this.cart = cart;
      }
    });
  }

  removeItem(cartItemId: number): void {
    this.cartService.removeItem(cartItemId).subscribe({
      next: () => {
        this.loadCart();
      }
    });
  }

  clearCart(): void {
    this.cartService.clear().subscribe({
      next: () => {
        this.loadCart();
      }
    });
  }

  createOrder(): void {
    if (!this.cart?.items.length) return;
    this.orderService.create(this.checkout).subscribe({
      next: (order: Order) => {
        // Success - cart will be cleared by the order creation process
        this.loadCart();
      }
    });
  }

  getTotalPrice(): number {
    return this.cart?.items.reduce((sum: number, item: CartItem) => sum + item.totalPrice, 0) ?? 0;
  }

  getItemCount(): number {
    return this.cart?.items.reduce((sum: number, item: CartItem) => sum + item.quantity, 0) ?? 0;
  }
}