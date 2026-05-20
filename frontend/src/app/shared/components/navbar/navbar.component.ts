import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../core/models/user.model';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <nav class="navbar">
      <div class="nav-container">
        <a routerLink="/" class="nav-brand">
          <span class="brand-icon">🌿</span>
          <span class="brand-text">TechNest</span>
        </a>

        <ul class="nav-menu">
          <li><a routerLink="/">Shop</a></li>
          <li><a href="#new">New Arrivals</a></li>
          <li><a href="#best">Best Sellers</a></li>
          <li><a href="#about">About</a></li>
          <li><a href="#contact">Contact</a></li>
        </ul>

        <div class="nav-icons">
          <button class="icon-btn" title="Search">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white">
              <circle cx="11" cy="11" r="8"></circle>
              <path d="m21 21-4.35-4.35"></path>
            </svg>
          </button>
          <button class="icon-btn" title="Account">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
              <circle cx="12" cy="7" r="4"></circle>
            </svg>
          </button>
          <button class="icon-btn cart-btn" title="Cart" routerLink="/shop">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white">
              <circle cx="9" cy="21" r="1"></circle>
              <circle cx="20" cy="21" r="1"></circle>
              <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
            </svg>
            <span class="cart-badge">{{ cartCount }}</span>
          </button>
          <ng-container *ngIf="currentUser$ | async as user; else notLoggedIn">
            <button class="btn btn-logout" (click)="logout()">Logout</button>
          </ng-container>
          <ng-template #notLoggedIn>
            <button class="btn btn-secondary btn-sm" routerLink="/auth/login">Sign In</button>
          </ng-template>
        </div>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      background: #1a1a1a;
      padding: 1rem 2rem;
      position: sticky;
      top: 0;
      z-index: 100;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .nav-container {
      max-width: 1400px;
      margin: 0 auto;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 3rem;
    }

    .nav-brand {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      cursor: pointer;
      font-weight: 600;
      text-decoration: none;

      .brand-icon {
        font-size: 1.5rem;
      }

      .brand-text {
        color: white;
        font-size: 1.25rem;
        font-weight: 700;
      }
    }

    .nav-menu {
      display: flex;
      gap: 2rem;
      list-style: none;
      margin: 0;
      padding: 0;
      flex: 1;

      li a {
        color: white;
        text-decoration: none;
        font-size: 0.9rem;
        cursor: pointer;
        transition: opacity 0.2s;

        &:hover {
          opacity: 0.7;
        }
      }
    }

    .nav-icons {
      display: flex;
      gap: 1rem;
      align-items: center;
    }

    .icon-btn {
      background: none;
      border: none;
      cursor: pointer;
      padding: 0.5rem;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      transition: transform 0.2s;

      &:hover {
        transform: scale(1.1);
      }
    }

    .cart-badge {
      position: absolute;
      top: 0;
      right: 0;
      background: #22c55e;
      color: #1a1a1a;
      font-size: 0.7rem;
      font-weight: 700;
      min-width: 1.2rem;
      height: 1.2rem;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .btn {
      padding: 0.6rem 1.2rem;
      border: none;
      border-radius: 6px;
      font-size: 0.85rem;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
      text-decoration: none;

      &.btn-secondary {
        background: white;
        color: #1a1a1a;
        border: 1px solid #1a1a1a;

        &:hover {
          background: #1a1a1a;
          color: white;
        }
      }

      &.btn-logout {
        background: #ef4444;
        color: white;

        &:hover {
          background: #dc2626;
        }
      }

      &.btn-sm {
        padding: 0.5rem 1rem;
        font-size: 0.8rem;
      }
    }

    @media (max-width: 768px) {
      .nav-container {
        flex-wrap: wrap;
        gap: 1rem;
      }

      .nav-menu {
        order: 3;
        flex-basis: 100%;
        gap: 1rem;
      }

      .nav-brand {
        flex: 0 0 auto;
      }
    }
  `]
})
export class NavbarComponent {
  currentUser$ = this.authService.currentUser$;
  cartCount = 3; // TODO: Get from cart service

  constructor(private authService: AuthService) { }

  logout(): void {
    this.authService.logout();
  }
}
