import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse, PageResponse, Product, ProductFilter, ProductRequest } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly API_URL = 'http://localhost:8080/api/v1/products';

  constructor(private http: HttpClient) {}

  getAll(filter: ProductFilter = {}): Observable<PageResponse<Product>> {
    let params = new HttpParams();
    if (filter.search) params = params.set('search', filter.search);
    if (filter.category) params = params.set('category', filter.category);
    if (filter.status) params = params.set('status', filter.status);
    params = params.set('page', filter.page ?? 0);
    params = params.set('size', filter.size ?? 10);
    params = params.set('sortBy', filter.sortBy ?? 'createdAt');
    params = params.set('sortDir', filter.sortDir ?? 'desc');

    return this.http.get<ApiResponse<PageResponse<Product>>>(this.API_URL, { params })
      .pipe(map(res => res.data));
  }

  getById(id: number): Observable<Product> {
    return this.http.get<ApiResponse<Product>>(`${this.API_URL}/${id}`)
      .pipe(map(res => res.data));
  }

  create(product: ProductRequest): Observable<Product> {
    return this.http.post<ApiResponse<Product>>(this.API_URL, product)
      .pipe(map(res => res.data));
  }

  update(id: number, product: ProductRequest): Observable<Product> {
    return this.http.put<ApiResponse<Product>>(`${this.API_URL}/${id}`, product)
      .pipe(map(res => res.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`)
      .pipe(map(() => void 0));
  }
}
