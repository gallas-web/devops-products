import { Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  if (authService.currentUser) {
    return true;
  } else {
    // Store the attempted URL for redirect after login
    authService.loginUrl = state.url;
    router.navigate(['/']);
    return false;
  }
};

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  const currentUser = authService.currentUser;
  if (currentUser && currentUser.role === 'ADMIN') {
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};