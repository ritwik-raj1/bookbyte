import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <div class="container">
      <div class="hero-section text-center">
        <h1>Welcome to BookByte</h1>
        <p class="hero-subtitle">Your one-stop destination for all things books</p>
        <div class="hero-actions">
          <button mat-raised-button color="primary" routerLink="/books" class="hero-button">
            <mat-icon>book</mat-icon>
            Browse Books
          </button>
          <button mat-stroked-button routerLink="/register" class="hero-button">
            <mat-icon>person_add</mat-icon>
            Join Now
          </button>
        </div>
      </div>

      <div class="features-section">
        <h2 class="text-center">Why Choose BookByte?</h2>
        <div class="features-grid">
          <mat-card class="feature-card">
            <mat-card-header>
              <mat-icon mat-card-avatar>search</mat-icon>
              <mat-card-title>Easy Search</mat-card-title>
            </mat-card-header>
            <mat-card-content>
              <p>Find your favorite books quickly with our advanced search and filtering options.</p>
            </mat-card-content>
          </mat-card>

          <mat-card class="feature-card">
            <mat-card-header>
              <mat-icon mat-card-avatar>shopping_cart</mat-icon>
              <mat-card-title>Simple Shopping</mat-card-title>
            </mat-card-header>
            <mat-card-content>
              <p>Add books to your cart and checkout seamlessly with our user-friendly interface.</p>
            </mat-card-content>
          </mat-card>

          <mat-card class="feature-card">
            <mat-card-header>
              <mat-icon mat-card-avatar>local_shipping</mat-icon>
              <mat-card-title>Fast Delivery</mat-card-title>
            </mat-card-header>
            <mat-card-content>
              <p>Get your books delivered to your doorstep with our reliable shipping service.</p>
            </mat-card-content>
          </mat-card>

          <mat-card class="feature-card">
            <mat-card-header>
              <mat-icon mat-card-avatar>security</mat-icon>
              <mat-card-title>Secure Shopping</mat-card-title>
            </mat-card-header>
            <mat-card-content>
              <p>Shop with confidence knowing your information is protected with bank-level security.</p>
            </mat-card-content>
          </mat-card>
        </div>
      </div>

      <div class="categories-section">
        <h2 class="text-center">Popular Categories</h2>
        <div class="categories-grid">
          <mat-card class="category-card" routerLink="/books">
            <mat-card-content>
              <h3>Fiction</h3>
              <p>Explore imaginative worlds and compelling stories</p>
            </mat-card-content>
          </mat-card>

          <mat-card class="category-card" routerLink="/books">
            <mat-card-content>
              <h3>Non-Fiction</h3>
              <p>Discover knowledge and real-world insights</p>
            </mat-card-content>
          </mat-card>

          <mat-card class="category-card" routerLink="/books">
            <mat-card-content>
              <h3>Science Fiction</h3>
              <p>Journey into futuristic and speculative fiction</p>
            </mat-card-content>
          </mat-card>

          <mat-card class="category-card" routerLink="/books">
            <mat-card-content>
              <h3>Technology</h3>
              <p>Stay updated with the latest tech trends</p>
            </mat-card-content>
          </mat-card>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .hero-section {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 80px 20px;
      border-radius: 16px;
      margin-bottom: 40px;
    }

    .hero-subtitle {
      font-size: 20px;
      margin: 20px 0 40px 0;
      opacity: 0.9;
    }

    .hero-actions {
      display: flex;
      gap: 20px;
      justify-content: center;
      flex-wrap: wrap;
    }

    .hero-button {
      padding: 12px 24px;
      font-size: 16px;
    }

    .features-section {
      margin-bottom: 40px;
    }

    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 20px;
      margin-top: 30px;
    }

    .feature-card {
      text-align: center;
      padding: 20px;
      transition: transform 0.3s ease;
    }

    .feature-card:hover {
      transform: translateY(-5px);
    }

    .feature-card mat-icon {
      font-size: 48px;
      width: 48px;
      height: 48px;
      color: #667eea;
    }

    .categories-section {
      margin-bottom: 40px;
    }

    .categories-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      margin-top: 30px;
    }

    .category-card {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .category-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0,0,0,0.15);
    }

    .category-card h3 {
      color: #667eea;
      margin-bottom: 10px;
    }

    h1 {
      font-size: 48px;
      margin-bottom: 20px;
      font-weight: 300;
    }

    h2 {
      font-size: 32px;
      margin-bottom: 30px;
      color: #333;
    }

    @media (max-width: 768px) {
      .hero-section {
        padding: 40px 20px;
      }

      h1 {
        font-size: 36px;
      }

      .hero-actions {
        flex-direction: column;
        align-items: center;
      }

      .features-grid,
      .categories-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class HomeComponent {}