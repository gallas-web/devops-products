import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../../core/services/admin.service';
import { AdminUserDto } from '../../../core/models/admin.model';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzModalModule, NzModalService } from 'ng-zorro-antd/modal';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { UserUpdate } from '../../../core/models/user.model';
import { FormsModule } from '@angular/forms';
import { PageResponse } from '../../../core/models/product.model';

@Component({
  selector: 'app-users-page',
  standalone: true,
  imports: [
    CommonModule,
    NzTableModule,
    NzSpinModule,
    NzButtonModule,
    NzModalModule,
    NzIconModule,
    NzTagModule,
    NzToolTipModule,
    NzFormModule,
    NzEmptyModule,
    FormsModule
  ],
  templateUrl: './users-page.component.html',
  styleUrls: ['./users-page.component.scss']
})
export class UsersPageComponent implements OnInit {
  users: AdminUserDto[] = [];
  loading = true;
  isEditModalVisible = false;
  isAddModalVisible = false;
  currentUser: AdminUserDto | null = null;
  editUser: UserUpdate = {
    firstName: '',
    lastName: '',
    phone: '',
    address: '',
    city: '',
    zipCode: '',
    country: ''
  };

  private buildAdminUserUpdatePayload(): any {
    // The backend AdminUserDto is required by AdminService.updateUser.
    // We reconstruct it from the editable fields while keeping other AdminUserDto fields stable.
    if (!this.currentUser) {
      return null;
    }

    const u = this.editUser;
    return {
      id: this.currentUser.id,
      email: this.currentUser.email,
      enabled: this.currentUser.enabled,
      role: this.currentUser.role,
      orderCount: this.currentUser.orderCount,
      totalSpent: this.currentUser.totalSpent,
      createdAt: this.currentUser.createdAt,
      firstName: u.firstName,
      lastName: u.lastName,
      phone: u.phone,
      address: u.address,
      city: u.city,
      zipCode: u.zipCode,
      country: u.country
    };
  }

  constructor(
    private adminService: AdminService,
    private modal: NzModalService,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.adminService.getAllUsers().subscribe({
      next: (page: PageResponse<AdminUserDto>) => {
        this.users = page.content;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.message.error('Erreur lors du chargement des utilisateurs');
      }
    });
  }

  showEditModal(user: AdminUserDto): void {
    this.currentUser = user;
    this.editUser = {
      firstName: user.firstName,
      lastName: user.lastName,
      phone: (user as any).phone || '',
      address: (user as any).address || '',
      city: (user as any).city || '',
      zipCode: (user as any).zipCode || '',
      country: (user as any).country || ''
    };
    this.isEditModalVisible = true;
  }

  showAddModal(): void {
    this.currentUser = null;
    this.editUser = {
      firstName: '',
      lastName: '',
      phone: '',
      address: '',
      city: '',
      zipCode: '',
      country: ''
    };
    this.isAddModalVisible = true;
  }

  handleEditOk(): void {
    if (this.currentUser) {
          const payload = this.buildAdminUserUpdatePayload();
          if (!payload) return;

          this.adminService.updateUser(this.currentUser.id, payload).subscribe({
        next: () => {
          this.message.success('Utilisateur mis à jour avec succès');
          this.loadUsers();
          this.isEditModalVisible = false;
        },
        error: () => {
          this.message.error('Erreur lors de la mise à jour de l\'utilisateur');
        }
      });
    }
  }

  handleAddOk(): void {
    // In a real app, this would call an admin service to create a user
    // For now, we'll simulate it
    this.message.success('Utilisateur créé avec succès');
    this.loadUsers();
    this.isAddModalVisible = false;
  }

  handleCancel(): void {
    this.isEditModalVisible = false;
    this.isAddModalVisible = false;
  }

  toggleUserStatus(user: AdminUserDto): void {
    this.adminService.toggleUserStatus(user.id).subscribe({
      next: () => {
        const newStatus = !user.enabled;
        user.enabled = newStatus;
        this.message.success(`Utilisateur ${newStatus ? 'activé' : 'désactivé'} avec succès`);
      },
      error: () => {
        this.message.error('Erreur lors du changement de statut');
      }
    });
  }

  getStatusText(active: boolean): string {
    return active ? 'Actif' : 'Inactif';
  }

  getStatusColor(active: boolean): string {
    return active ? 'success' : 'error';
  }
}