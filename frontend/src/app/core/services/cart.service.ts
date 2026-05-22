import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../models/product.model';
import { AddToCartRequest, Cart, UpdateCartItemRequest } from '../models/cart.model';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly API_URL = '/api/v1/cart';
  private readonly cartSubject = new BehaviorSubject<Cart | null>(null);
  cart$ = this.cartSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadCart();
  }

  private loadCart(): void {
    this.http.get<ApiResponse<Cart>>(this.API_URL)
      .pipe(map(res => res.data))
      .subscribe(cart => {
        this.cartSubject.next(cart);
      });
  }

  getCart(): Observable<Cart> {
    return this.http.get<ApiResponse<Cart>>(this.API_URL).pipe(map(res => res.data));
  }

  addToCart(request: AddToCartRequest): Observable<Cart> {
    return this.http.post<ApiResponse<Cart>>(`${this.API_URL}/add`, request)
      .pipe(map(res => {
        const cart = res.data;
        this.cartSubject.next(cart);
        return cart;
      }));
  }

  updateItem(request: UpdateCartItemRequest): Observable<Cart> {
    return this.http.put<ApiResponse<Cart>>(`${this.API_URL}/update`, request)
      .pipe(map(res => {
        const cart = res.data;
        this.cartSubject.next(cart);
        return cart;
      }));
  }

  removeItem(cartItemId: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/item/${cartItemId}`)
      .pipe(map(() => {
        this.loadCart(); // Reload cart after deletion
        return void 0;
      }));
  }

  clear(): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/clear`)
      .pipe(map(() => {
        this.cartSubject.next(null);
        return void 0;
      }));
  }
}
