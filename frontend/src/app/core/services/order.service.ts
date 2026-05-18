import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse, PageResponse } from '../models/product.model';
import { CreateOrderRequest, Order, OrderFilter } from '../models/order.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly API_URL = '/api/v1/orders';

  constructor(private http: HttpClient) {}

  create(request: CreateOrderRequest): Observable<Order> {
    return this.http.post<ApiResponse<Order>>(this.API_URL, request).pipe(map(res => res.data));
  }

  getMyOrders(filter: OrderFilter = {}): Observable<PageResponse<Order>> {
    let params = new HttpParams()
      .set('page', filter.page ?? 0)
      .set('size', filter.size ?? 10)
      .set('sortBy', filter.sortBy ?? 'createdAt')
      .set('sortDir', filter.sortDir ?? 'desc');
    if (filter.status) params = params.set('status', filter.status);
    if (filter.paymentStatus) params = params.set('paymentStatus', filter.paymentStatus);
    return this.http.get<ApiResponse<PageResponse<Order>>>(this.API_URL, { params }).pipe(map(res => res.data));
  }
}
