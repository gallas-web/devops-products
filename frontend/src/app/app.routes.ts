import { Routes } from '@angular/router';

export const routes: Routes = [
  // Public Routes
  {
    path: '',
    loadComponent: () =>
      import('./features/shop/pages/shop-page.component')
        .then(m => m.ShopPageComponent)
  },
  {
    path: 'product/:id',
    loadComponent: () =>
      import('./features/products/pages/product-detail/product-detail.component')
        .then(m => m.ProductDetailComponent)
  },

  // Auth Routes
  {
    path: 'auth/login',
    loadComponent: () =>
      import('./features/auth/pages/login-page.component')
        .then(m => m.LoginComponent)
  },
  {
    path: 'auth/register',
    loadComponent: () =>
      import('./features/auth/pages/register-page.component')
        .then(m => m.RegisterComponent)
  },

  // Admin Routes (TODO: Add AuthGuard)
  {
    path: 'admin/products',
    loadComponent: () =>
      import('./features/products/pages/products-page.component')
        .then(m => m.ProductsPageComponent)
  },
  {
    path: 'admin/dashboard',
    loadComponent: () =>
      import('./features/admin/pages/dashboard-page.component')
        .then(m => m.AdminDashboardComponent)
  },

  // Fallback
  { path: '**', redirectTo: '' }
];
