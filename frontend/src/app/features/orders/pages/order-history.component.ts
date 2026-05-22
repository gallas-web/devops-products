import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { OrderService } from '../../../core/services/order.service';
import { Order } from '../../../core/models/order.model';
import { NzTableModule, NzTableQueryParams } from 'ng-zorro-antd/table';
import { NzPaginationModule } from 'ng-zorro-antd/pagination';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { ORDER_STATUSES, PAYMENT_STATUSES } from '../../../core/models/order.model';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/product.model';

@Component({
  selector: 'app-order-history',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    // Needed for [(ngModel)]
    FormsModule,
    NzTableModule,
    NzPaginationModule,
    NzSpinModule,
    NzEmptyModule,
    NzBadgeModule,
    NzButtonModule,
    NzDropDownModule,
    NzOptionComponent,
    NzSelectModule,
    NzIconModule
  ],
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent {
  orders$: Observable<PageResponse<Order>> | null = null;
  loading = false;
  pageIndex = 0;
  pageSize = 10;
  total = 0;
  sortValue: string | null = null;
  sortKey: string | null = null;
  filterStatus: string | null = null;
  filterPaymentStatus: string | null = null;
  
  orderStatuses = ORDER_STATUSES;
  paymentStatuses = PAYMENT_STATUSES;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    const filter: any = {
      page: this.pageIndex,
      size: this.pageSize,
      sortBy: this.sortKey ?? 'createdAt',
      sortDir: this.sortValue === 'desc' ? 'desc' : 'asc'
    };
    
    if (this.filterStatus) {
      filter.status = this.filterStatus;
    }
    
    if (this.filterPaymentStatus) {
      filter.paymentStatus = this.filterPaymentStatus;
    }
    
    this.orders$ = this.orderService.getMyOrders(filter);
  }

  onQueryParamsChange(params: NzTableQueryParams | any): void {
    const p = params as NzTableQueryParams;
    const { pageSize, pageIndex, sort, filter } = p;
    const currentSort = sort.find((item: { key: string; value: string | null }) => item.value !== null);
    this.sortKey = currentSort ? currentSort.key : null;
    this.sortValue = currentSort ? currentSort.value : null;

    this.pageSize = pageSize;
    this.pageIndex = pageIndex;
    
    // Apply filters
    const statusFilter = (filter as any[]).find((item: any) => item.key === 'status');
    this.filterStatus = statusFilter ? statusFilter.value.join(',') : null;
    
    const paymentStatusFilter = (filter as any[]).find((item: any) => item.key === 'paymentStatus');
    this.filterPaymentStatus = paymentStatusFilter ? paymentStatusFilter.value.join(',') : null;
    
    this.loadOrders();
  }

  getStatusLabel(status: string): string {
    const statusObj = this.orderStatuses.find(s => s.value === status);
    return statusObj ? statusObj.label : status;
  }

  getPaymentStatusLabel(status: string): string {
    const statusObj = this.paymentStatuses.find(s => s.value === status);
    return statusObj ? statusObj.label : status;
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDING': return 'processing';
      case 'CONFIRMED': return 'processing';
      case 'SHIPPED': return 'default';
      case 'DELIVERED': return 'success';
      case 'CANCELLED': return 'error';
      case 'REFUNDED': return 'warning';
      default: return 'default';
    }
  }

  getPaymentStatusColor(status: string): string {
    switch (status) {
      case 'PENDING': return 'warning';
      case 'PAID': return 'success';
      case 'FAILED': return 'error';
      case 'REFUNDED': return 'error';
      default: return 'default';
    }
  }
}