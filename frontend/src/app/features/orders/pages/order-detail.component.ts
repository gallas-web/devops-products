import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { OrderService } from '../../../core/services/order.service';
import { Order } from '../../../core/models/order.model';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { ORDER_STATUSES, PAYMENT_STATUSES } from '../../../core/models/order.model';
import { CommonModule } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    NzSpinModule,
    NzCardModule,
    NzTableModule,
    NzBadgeModule,
    NzEmptyModule,
    NzTagModule,
    NzButtonModule,
    NzIconModule
  ],
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent implements OnInit {
  order: Order | null = null;
  loading = true;
  orderId: number = 0;
  
  orderStatuses = ORDER_STATUSES;
  paymentStatuses = PAYMENT_STATUSES;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadOrder();
  }

  loadOrder(): void {
    // Since we don't have a getOrderById method, we'll simulate getting it
    // In a real implementation, you'd call the API to get a specific order
    this.loading = true;
    // For demo purposes, we'll just set loading to false after a short delay
    setTimeout(() => {
      this.loading = false;
      // In a real app, you would fetch the actual order data here
    }, 500);
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