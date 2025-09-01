import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';

import { AuthService } from './services/auth.service';
import { CartService } from './services/cart.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatBadgeModule
  ],
  template: `
    <mat-toolbar color="primary">
      <span routerLink="/home" style="cursor: pointer; font-size: 24px; font-weight: bold;">
        📚 BookByte
      </span>
      
      <span class="spacer"></span>
      
      <button mat-button routerLink="/books">Books</button>
      
      <button mat-button routerLink="/cart" [matBadge]="cartItemCount" matBadgeColor="accent">
        <mat-icon>shopping_cart</mat-icon>
        Cart
      </button>
      
      <ng-container *ngIf="!isLoggedIn; else userMenu">
        <button mat-button routerLink="/login">Login</button>
        <button mat-button routerLink="/register">Register</button>
      </ng-container>
      
      <ng-template #userMenu>
        <ng-container *ngIf="isAdmin; else customerMenu">
          <button mat-button routerLink="/admin">Admin Dashboard</button>
        </ng-container>
        
        <ng-template #customerMenu>
          <button mat-button routerLink="/orders">My Orders</button>
        </ng-template>
        
        <button mat-button (click)="logout()">
          <mat-icon>logout</mat-icon>
          Logout
        </button>
      </ng-container>
    </mat-toolbar>
    
    <main style="padding: 20px;">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .spacer {
      flex: 1 1 auto;
    }
    
    mat-toolbar {
      position: sticky;
      top: 0;
      z-index: 1000;
    }
    
    main {
      min-height: calc(100vh - 64px);
      background-color: #f5f5f5;
    }
  `]
})
export class AppComponent {
  isLoggedIn = false;
  isAdmin = false;
  cartItemCount = 0;

  constructor(
    private authService: AuthService,
    private cartService: CartService
  ) {
    this.authService.isLoggedIn$.subscribe(loggedIn => {
      this.isLoggedIn = loggedIn;
      this.isAdmin = this.authService.isAdmin();
    });
    
    this.cartService.cartItemCount$.subscribe(count => {
      this.cartItemCount = count;
    });
  }

  logout(): void {
    this.authService.logout();
  }
}