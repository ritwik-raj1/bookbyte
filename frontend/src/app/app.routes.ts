import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', loadComponent: () => import('./components/home/home.component').then(m => m.HomeComponent) },
  { path: 'login', loadComponent: () => import('./components/auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./components/auth/register/register.component').then(m => m.RegisterComponent) },
  { 
    path: 'books', 
    loadComponent: () => import('./components/books/book-list/book-list.component').then(m => m.BookListComponent) 
  },
  { 
    path: 'books/:id', 
    loadComponent: () => import('./components/books/book-detail/book-detail.component').then(m => m.BookDetailComponent) 
  },
  { 
    path: 'cart', 
    loadComponent: () => import('./components/cart/cart.component').then(m => m.CartComponent),
    canActivate: [authGuard]
  },
  { 
    path: 'orders', 
    loadComponent: () => import('./components/orders/order-list/order-list.component').then(m => m.OrderListComponent),
    canActivate: [authGuard]
  },
  { 
    path: 'orders/:id', 
    loadComponent: () => import('./components/orders/order-detail/order-detail.component').then(m => m.OrderDetailComponent),
    canActivate: [authGuard]
  },
  { 
    path: 'admin', 
    loadComponent: () => import('./components/admin/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent),
    canActivate: [adminGuard]
  },
  { 
    path: 'admin/books', 
    loadComponent: () => import('./components/admin/book-management/book-management.component').then(m => m.BookManagementComponent),
    canActivate: [adminGuard]
  },
  { 
    path: 'admin/orders', 
    loadComponent: () => import('./components/admin/order-management/order-management.component').then(m => m.OrderManagementComponent),
    canActivate: [adminGuard]
  },
  { 
    path: 'admin/customers', 
    loadComponent: () => import('./components/admin/customer-management/customer-management.component').then(m => m.CustomerManagementComponent),
    canActivate: [adminGuard]
  },
  { path: '**', redirectTo: '/home' }
];