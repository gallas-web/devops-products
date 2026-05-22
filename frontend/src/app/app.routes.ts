import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  // Public Routes
  {
    path: '',
    loadComponent: () =>
      import('./features/shop/pages/shop-page.component')
        .then(m => m.ShopPageComponent)
  },
  {
    path: 'shop',
    loadComponent: () =>
      import('./features/shop/components/shopping-cart/shopping-cart.component')
        .then(m => m.ShoppingCartComponent)
  },
  {
    path: 'orders',
    loadComponent: () =>
      import('./features/orders/pages/order-history.component')
        .then(m => m.OrderHistoryComponent),
    canActivate: [authGuard]
  },
  {
    path: 'orders/:id',
    loadComponent: () =>
      import('./features/orders/pages/order-detail.component')
        .then(m => m.OrderDetailComponent),
    canActivate: [authGuard]
  },
  {
    path: 'product/:id',
    loadComponent: () =>
      import('./features/products/pages/product-detail/product-detail.component')
        .then(m => m.ProductDetailComponent)
  },

  // Redirect /auth/login and /auth/register to home with auth modal
  { path: 'auth/login', redirectTo: '' },
  { path: 'auth/register', redirectTo: '' },

  // Admin Routes (with AuthGuard)
  {
    path: 'admin/products',
    loadComponent: () =>
      import('./features/products/pages/products-page.component')
        .then(m => m.ProductsPageComponent),
    canActivate: [authGuard, adminGuard]
  },
  {
    path: 'admin/dashboard',
    loadComponent: () =>
      import('./features/admin/pages/dashboard-page.component')
        .then(m => m.AdminDashboardComponent),
    canActivate: [authGuard, adminGuard]
  },
  {
    path: 'admin/users',
    loadComponent: () =>
      import('./features/admin/pages/users-page.component')
        .then(m => m.UsersPageComponent),
    canActivate: [authGuard, adminGuard]
  },
  {
    path: 'admin/orders',
    loadComponent: () =>
      import('./features/admin/pages/orders-page.component')
        .then(m => m.AdminOrdersPageComponent),
    canActivate: [authGuard, adminGuard]
  },

  // Fallback
  { path: '**', redirectTo: '' }
];
