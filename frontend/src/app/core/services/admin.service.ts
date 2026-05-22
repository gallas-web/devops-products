import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse, PageResponse } from '../models/product.model';
import { AdminDashboardStatsDto, AdminOrderDto, AdminUserDto } from '../models/admin.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = '/api/v1/admin';

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<AdminDashboardStatsDto> {
    return this.http.get<ApiResponse<AdminDashboardStatsDto>>(`${this.apiUrl}/dashboard/stats`)
      .pipe(map(res => res.data));
  }

  getRecentOrders(): Observable<AdminOrderDto[]> {
    return this.http.get<ApiResponse<AdminOrderDto[]>>(`${this.apiUrl}/orders?size=5`)
      .pipe(map(res => res.data));
  }

  getRecentUsers(): Observable<AdminUserDto[]> {
    return this.http.get<ApiResponse<AdminUserDto[]>>(`${this.apiUrl}/users?size=5`)
      .pipe(map(res => res.data));
  }

  getAllUsers(page: number = 0, size: number = 20): Observable<PageResponse<AdminUserDto>> {
    const params = new URLSearchParams();
    params.set('page', page.toString());
    params.set('size', size.toString());
    return this.http.get<ApiResponse<PageResponse<AdminUserDto>>>(`${this.apiUrl}/users?${params.toString()}`)
      .pipe(map(res => res.data));
  }

  getAllOrders(page: number = 0, size: number = 20): Observable<PageResponse<AdminOrderDto>> {
    const params = new URLSearchParams();
    params.set('page', page.toString());
    params.set('size', size.toString());
    return this.http.get<ApiResponse<PageResponse<AdminOrderDto>>>(`${this.apiUrl}/orders?${params.toString()}`)
      .pipe(map(res => res.data));
  }

  updateOrderStatus(orderId: number, status: string): Observable<AdminOrderDto> {
    return this.http.put<ApiResponse<AdminOrderDto>>(`${this.apiUrl}/orders/${orderId}/status/${status}`, null)
      .pipe(map(res => res.data));
  }

  toggleUserStatus(userId: number): Observable<AdminUserDto> {
    return this.http.put<ApiResponse<AdminUserDto>>(`${this.apiUrl}/users/${userId}/toggle-status`, null)
      .pipe(map(res => res.data));
  }

  updateUser(userId: number, user: AdminUserDto): Observable<AdminUserDto> {
    return this.http.put<ApiResponse<AdminUserDto>>(`${this.apiUrl}/users/${userId}`, user)
      .pipe(map(res => res.data));
  }
}