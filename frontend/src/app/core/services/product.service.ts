import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse, PageResponse, Product, ProductDetail, ProductFilter, ProductRequest, Category, Review, CreateReviewRequest } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly API_URL = '/api/v1/products';
  private readonly CATEGORIES_URL = '/api/v1/categories';
  private readonly REVIEWS_URL = '/api/v1/reviews';

  constructor(private http: HttpClient) {}

  // Produits
  getAll(filter: ProductFilter = {}): Observable<PageResponse<Product>> {
    let params = new HttpParams();
    if (filter.search) params = params.set('search', filter.search);
    if (filter.categoryId) params = params.set('categoryId', filter.categoryId.toString());
    if (filter.status) params = params.set('status', filter.status);
    params = params.set('page', filter.page ?? 0);
    params = params.set('size', filter.size ?? 10);
    params = params.set('sortBy', filter.sortBy ?? 'createdAt');
    params = params.set('sortDir', filter.sortDir ?? 'desc');

    return this.http.get<ApiResponse<PageResponse<Product>>>(this.API_URL, { params })
      .pipe(map(res => res.data));
  }

  getById(id: number): Observable<ProductDetail> {
    return this.http.get<ApiResponse<ProductDetail>>(`${this.API_URL}/${id}`)
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

  // Catégories
  getCategories(): Observable<Category[]> {
    return this.http.get<ApiResponse<Category[]>>(`${this.CATEGORIES_URL}/active`)
      .pipe(map(res => res.data));
  }

  getCategoryById(id: number): Observable<Category> {
    return this.http.get<ApiResponse<Category>>(`${this.CATEGORIES_URL}/${id}`)
      .pipe(map(res => res.data));
  }

  createCategory(category: Category): Observable<Category> {
    return this.http.post<ApiResponse<Category>>(this.CATEGORIES_URL, category)
      .pipe(map(res => res.data));
  }

  updateCategory(id: number, category: Category): Observable<Category> {
    return this.http.put<ApiResponse<Category>>(`${this.CATEGORIES_URL}/${id}`, category)
      .pipe(map(res => res.data));
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.CATEGORIES_URL}/${id}`)
      .pipe(map(() => void 0));
  }

  // Avis/Reviews
  getProductReviews(productId: number, page: number = 0, size: number = 10): Observable<PageResponse<Review>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);
    return this.http.get<ApiResponse<PageResponse<Review>>>(`${this.REVIEWS_URL}/product/${productId}`, { params })
      .pipe(map(res => res.data));
  }

  addReview(productId: number, review: CreateReviewRequest): Observable<Review> {
    return this.http.post<ApiResponse<Review>>(`${this.REVIEWS_URL}/product/${productId}`, review)
      .pipe(map(res => res.data));
  }

  updateReview(reviewId: number, review: CreateReviewRequest): Observable<Review> {
    return this.http.put<ApiResponse<Review>>(`${this.REVIEWS_URL}/${reviewId}`, review)
      .pipe(map(res => res.data));
  }

  deleteReview(reviewId: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.REVIEWS_URL}/${reviewId}`)
      .pipe(map(() => void 0));
  }
}
