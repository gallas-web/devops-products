import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzStatisticModule } from 'ng-zorro-antd/statistic';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { AdminService } from '../../../core/services/admin.service';
import { AdminDashboardStatsDto, AdminOrderDto, AdminUserDto } from '../../../core/models/admin.model';
import { FcfaPipe } from '../../../shared/pipes/fcfa.pipe';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    NzCardModule,
    NzButtonModule,
    NzGridModule,
    NzStatisticModule,
    NzIconModule,
    NzTableModule,
    NzTagModule,
    NzSpinModule,
    FcfaPipe
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  stats: AdminDashboardStatsDto | null = null;
  recentOrders: AdminOrderDto[] = [];
  recentUsers: AdminUserDto[] = [];
  loading = true;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.adminService.getDashboardStats().subscribe({
      next: stats => {
        this.stats = stats;
        this.loading = false;
      },
      error: () => this.loading = false
    });
    this.adminService.getRecentOrders().subscribe({
      next: orders => this.recentOrders = orders,
      error: () => {}
    });
    this.adminService.getRecentUsers().subscribe({
      next: users => this.recentUsers = users,
      error: () => {}
    });
  }

  getOrderStatusColor(status: string): string {
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

  getOrderStatusLabel(status: string): string {
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
}