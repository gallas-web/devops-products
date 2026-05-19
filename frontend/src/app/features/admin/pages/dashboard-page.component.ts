import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzButtonModule } from 'ng-zorro-antd/button';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, NzCardModule, NzButtonModule],
  template: `
    <div style="padding: 24px; max-width: 900px; margin: 0 auto;">
      <h1 style="margin-bottom: 16px;">Admin Dashboard</h1>
      <nz-card>
        <p style="margin: 0;">
          Page placeholder: component was missing and prevented the app from building.
        </p>
        <div style="margin-top: 16px;">
          <button nz-button nzType="default" routerLink="/admin/products">Gérer les produits</button>
        </div>
      </nz-card>
    </div>
  `
})
export class AdminDashboardComponent {}

