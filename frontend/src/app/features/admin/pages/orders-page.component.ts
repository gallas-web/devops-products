import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../../core/services/admin.service';
import { AdminOrderDto } from '../../../core/models/admin.model';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzModalModule, NzModalService } from 'ng-zorro-antd/modal';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { CommonModule } from '@angular/common';
import { PageResponse } from '../../../core/models/product.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-orders-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    NzTableModule,
    NzSpinModule,
    NzButtonModule,
    NzModalModule,
    NzIconModule,
    NzTagModule,
    NzToolTipModule,
    NzOptionComponent,
    NzSelectModule,
    NzEmptyModule
  ],
  templateUrl: './orders-page.component.html',
  styleUrls: ['./orders-page.component.scss']
})
export class AdminOrdersPageComponent implements OnInit {
  orders: AdminOrderDto[] = [];
  loading = true;
  isDetailModalVisible = false;
  currentOrder: AdminOrderDto | null = null;
  tempStatuses: { [key: number]: string } = {};
  orderStatuses = [
    { label: 'En attente', value: 'PENDING' },
    { label: 'Confirmée', value: 'CONFIRMED' },
    { label: 'Expédiée', value: 'SHIPPED' },
    { label: 'Livrée', value: 'DELIVERED' },
    { label: 'Annulée', value: 'CANCELLED' },
    { label: 'Remboursée', value: 'REFUNDED' }
  ];

  constructor(
    private adminService: AdminService,
    private modal: NzModalService,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.adminService.getAllOrders().subscribe({
      next: (response) => {
        this.orders = response.content || [];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.message.error('Erreur lors du chargement des commandes');
      }
    });
  }

  showOrderDetails(order: AdminOrderDto): void {
    this.currentOrder = order;
    this.isDetailModalVisible = true;
  }

  handleDetailCancel(): void {
    this.isDetailModalVisible = false;
    this.currentOrder = null;
  }

  updateOrderStatus(orderId: number, status: string): void {
    this.adminService.updateOrderStatus(orderId, status).subscribe({
      next: () => {
        const order = this.orders.find(o => o.id === orderId);
        if (order) {
          order.status = status;
        }
        this.message.success('Statut de la commande mis à jour avec succès');
        if (this.currentOrder && this.currentOrder.id === orderId) {
          this.currentOrder.status = status;
        }
      },
      error: () => {
        this.message.error('Erreur lors de la mise à jour du statut');
      }
    });
  }

  getStatusColor(status: string): string {
    const colors: Record<string, string> = {
      PENDING: 'processing',
      CONFIRMED: 'success',
      SHIPPED: 'blue',
      DELIVERED: 'green',
      CANCELLED: 'error',
      REFUNDED: 'warning'
    };
    return colors[status] || 'default';
  }

  getStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      PENDING: 'En attente',
      CONFIRMED: 'Confirmée',
      SHIPPED: 'Expédiée',
      DELIVERED: 'Livrée',
      CANCELLED: 'Annulée',
      REFUNDED: 'Remboursée'
    };
    return labels[status] || status;
  }

  getPaymentStatusColor(status: string): string {
    const colors: Record<string, string> = {
      PENDING: 'warning',
      PAID: 'success',
      FAILED: 'error',
      REFUNDED: 'error'
    };
    return colors[status] || 'default';
  }

  getPaymentStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      PENDING: 'En attente',
      PAID: 'Payée',
      FAILED: 'Échouée',
      REFUNDED: 'Remboursée'
    };
    return labels[status] || status;
  }
}