import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';

import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <div class="container">
      <div class="form-container">
        <mat-card>
          <mat-card-header>
            <mat-card-title class="text-center">Create Your BookByte Account</mat-card-title>
          </mat-card-header>
          
          <mat-card-content>
            <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
              <mat-form-field class="full-width">
                <mat-label>Full Name</mat-label>
                <input matInput formControlName="name" placeholder="Enter your full name">
                <mat-error *ngIf="registerForm.get('name')?.hasError('required')">
                  Name is required
                </mat-error>
                <mat-error *ngIf="registerForm.get('name')?.hasError('minlength')">
                  Name must be at least 2 characters
                </mat-error>
              </mat-form-field>

              <mat-form-field class="full-width">
                <mat-label>Email</mat-label>
                <input matInput type="email" formControlName="email" placeholder="Enter your email">
                <mat-error *ngIf="registerForm.get('email')?.hasError('required')">
                  Email is required
                </mat-error>
                <mat-error *ngIf="registerForm.get('email')?.hasError('email')">
                  Please enter a valid email
                </mat-error>
              </mat-form-field>

              <mat-form-field class="full-width">
                <mat-label>Age</mat-label>
                <input matInput type="number" formControlName="age" placeholder="Enter your age">
                <mat-error *ngIf="registerForm.get('age')?.hasError('required')">
                  Age is required
                </mat-error>
                <mat-error *ngIf="registerForm.get('age')?.hasError('min')">
                  You must be at least 18 years old
                </mat-error>
              </mat-form-field>

              <mat-form-field class="full-width">
                <mat-label>Address</mat-label>
                <textarea matInput formControlName="address" placeholder="Enter your address" rows="3"></textarea>
                <mat-error *ngIf="registerForm.get('address')?.hasError('required')">
                  Address is required
                </mat-error>
              </mat-form-field>

              <mat-form-field class="full-width">
                <mat-label>Password</mat-label>
                <input matInput type="password" formControlName="password" placeholder="Enter your password">
                <mat-icon matSuffix>lock</mat-icon>
                <mat-error *ngIf="registerForm.get('password')?.hasError('required')">
                  Password is required
                </mat-error>
                <mat-error *ngIf="registerForm.get('password')?.hasError('minlength')">
                  Password must be at least 6 characters
                </mat-error>
              </mat-form-field>

              <mat-form-field class="full-width">
                <mat-label>Confirm Password</mat-label>
                <input matInput type="password" formControlName="confirmPassword" placeholder="Confirm your password">
                <mat-icon matSuffix>lock</mat-icon>
                <mat-error *ngIf="registerForm.get('confirmPassword')?.hasError('required')">
                  Password confirmation is required
                </mat-error>
                <mat-error *ngIf="registerForm.get('confirmPassword')?.hasError('passwordMismatch')">
                  Passwords do not match
                </mat-error>
              </mat-form-field>

              <button mat-raised-button color="primary" type="submit" class="full-width mt-20" 
                      [disabled]="registerForm.invalid || isLoading">
                <mat-icon *ngIf="!isLoading">person_add</mat-icon>
                <span *ngIf="isLoading">Creating account...</span>
                <span *ngIf="!isLoading">Create Account</span>
              </button>
            </form>

            <div class="text-center mt-20">
              <p>Already have an account? <a routerLink="/login" class="link">Login here</a></p>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .form-container {
      max-width: 500px;
      margin: 0 auto;
      padding: 20px;
    }

    mat-card {
      padding: 20px;
    }

    mat-card-title {
      font-size: 24px;
      margin-bottom: 20px;
    }

    form {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .link {
      color: #667eea;
      text-decoration: none;
      font-weight: 500;
    }

    .link:hover {
      text-decoration: underline;
    }

    button[type="submit"] {
      height: 48px;
      font-size: 16px;
    }
  `]
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      age: ['', [Validators.required, Validators.min(18)]],
      address: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.isLoading = true;
      
      this.authService.register(this.registerForm.value).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.snackBar.open('Account created successfully! Please login.', 'Close', { duration: 5000 });
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.isLoading = false;
          const errorMessage = error.error?.error || 'Registration failed. Please try again.';
          this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
        }
      });
    }
  }
}