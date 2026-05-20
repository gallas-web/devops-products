/* eslint-disable @angular-eslint/prefer-inject */
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { debounceTime, Subject } from 'rxjs';

import { NzTableModule } from 'ng-zorro-antd/table';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzStatisticModule } from 'ng-zorro-antd/statistic';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzBadgeModule } from 'ng-zorro-antd/badge';

import { Category, PageResponse, Product, ProductFilter, ProductRequest, STATUSES } from '../../../core/models/product.model';
import { ProductService } from '../../../core/services/product.service';
import { ProductFormComponent } from '../components/product-form/product-form.component';
import { ActiveCountPipe, LowStockCountPipe } from '../../../shared/pipes/product.pipes';
import { FcfaPipe } from '../../../shared/pipes/fcfa.pipe';

@Component({
  selector: 'app-products-page',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    NzTableModule, NzButtonModule, NzInputModule, NzSelectModule,
    NzTagModule, NzPopconfirmModule, NzIconModule, NzSpaceModule,
    NzToolTipModule, NzEmptyModule, NzStatisticModule, NzCardModule,
    NzDividerModule, NzBadgeModule,
    ProductFormComponent,
    ActiveCountPipe, LowStockCountPipe,
    FcfaPipe
  ],
  templateUrl: './products-page.component.html',
  styleUrls: ['./products-page.component.scss']
})
export class ProductsPageComponent implements OnInit {
  pageData: PageResponse<Product> = {
    content: [], pageNumber: 0, pageSize: 10, totalElements: 0, totalPages: 0, last: true
  };

  filter: ProductFilter = { page: 0, size: 10, sortBy: 'createdAt', sortDir: 'desc' };
  categories: Category[] = [];
  statuses = STATUSES;

  formVisible = false;
  formLoading = false;
  selectedProduct: Product | null = null;
  tableLoading = false;

  private searchSubject = new Subject<string>();

  constructor(
    private productService: ProductService,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.productService.getCategories().subscribe(categories => this.categories = categories);
    this.loadProducts();
    this.searchSubject.pipe(debounceTime(400)).subscribe(() => {
      this.filter.page = 0;
      this.loadProducts();
    });
  }

  loadProducts(): void {
    this.tableLoading = true;
    this.productService.getAll(this.filter).subscribe({
      next: data => { this.pageData = data; this.tableLoading = false; },
      error: () => this.tableLoading = false
    });
  }

  onSearchChange(value: string): void {
    this.filter.search = value;
    this.searchSubject.next(value);
  }

  onFilterChange(): void {
    this.filter.page = 0;
    this.loadProducts();
  }

  onPageChange(page: number): void {
    this.filter.page = page - 1;
    this.loadProducts();
  }

  onPageSizeChange(size: number): void {
    this.filter.size = size;
    this.filter.page = 0;
    this.loadProducts();
  }

  openCreate(): void {
    this.selectedProduct = null;
    this.formVisible = true;
  }

  openEdit(product: Product): void {
    this.selectedProduct = { ...product };
    this.formVisible = true;
  }

  onFormSave(request: ProductRequest): void {
    this.formLoading = true;
    const op = this.selectedProduct
      ? this.productService.update(this.selectedProduct.id, request)
      : this.productService.create(request);

    op.subscribe({
      next: () => {
        this.message.success(this.selectedProduct ? 'Produit mis à jour !' : 'Produit créé !');
        this.formVisible = false;
        this.formLoading = false;
        this.loadProducts();
      },
      error: () => this.formLoading = false
    });
  }

  onFormCancel(): void {
    this.formVisible = false;
    this.selectedProduct = null;
  }

  deleteProduct(id: number): void {
    this.productService.delete(id).subscribe({
      next: () => {
        this.message.success('Produit supprimé avec succès');
        this.loadProducts();
      }
    });
  }

  clearFilters(): void {
    this.filter = { page: 0, size: 10, sortBy: 'createdAt', sortDir: 'desc' };
    this.loadProducts();
  }

  getStatusColor(status: string): string {
    return status === 'ACTIVE' ? 'success' : 'error';
  }

  getStatusLabel(status: string): string {
    return status === 'ACTIVE' ? 'Actif' : 'Inactif';
  }

  getStockColor(qty: number): string {
    if (qty === 0) return 'error';
    if (qty < 10) return 'warning';
    return 'success';
  }
}
