import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzIconModule } from 'ng-zorro-antd/icon';

import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest } from '../../../core/models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NzFormModule,
    NzInputModule,
    NzButtonModule,
    NzCardModule,
    NzSpinModule,
    NzIconModule,
    RouterLink
  ],
  template: `
    <div class="auth-container">
      <nz-card class="auth-card" [nzTitle]="'Se connecter'">
        <nz-spin [nzSpinning]="loading">
          <form nz-form [formGroup]="form" nzLayout="vertical" (ngSubmit)="onSubmit()">
            
            <nz-form-item>
              <nz-form-label nzRequired>Email</nz-form-label>
              <nz-form-control nzErrorTip="Veuillez entrer un email valide">
                <input nz-input formControlName="email" placeholder="votre@email.com" />
              </nz-form-control>
            </nz-form-item>


            <nz-form-item>
              <nz-form-label nzRequired>Mot de passe</nz-form-label>
              <nz-form-control nzErrorTip="Le mot de passe est obligatoire">
                <input nz-input type="password" formControlName="password" 
                       placeholder="Votre mot de passe" />
              </nz-form-control>
            </nz-form-item>


            <div style="text-align: right; margin-bottom: 16px">
              <a href="javascript:" routerLink="/auth/forgot" style="color: #1890ff">
                Mot de passe oublié?
              </a>
            </div>

            <nz-form-item>
              <button nz-button nzType="primary" nzBlock nzSize="large" type="submit" [disabled]="loading">
                Connexion
              </button>
            </nz-form-item>

            <div style="text-align: center; margin-top: 16px">
              <span>Pas encore de compte? </span>
              <a routerLink="/auth/register" style="color: #1890ff">S'inscrire maintenant</a>
            </div>
          </form>
        </nz-spin>
      </nz-card>
    </div>
  `,
  styles: [`
    .auth-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }
    .auth-card {
      width: 100%;
      max-width: 400px;
      border-radius: 8px;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    }
    nz-form-item {
      margin-bottom: 20px;
    }
  `]
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (!this.form.valid) {
      Object.values(this.form.controls).forEach(c => {
        c.markAsDirty();
        c.updateValueAndValidity({ onlySelf: true });
      });
      return;
    }

    this.loading = true;
    this.authService.login(this.form.value as LoginRequest).subscribe({
      next: () => {
        this.message.success('Bienvenue!');
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.message.error(err.error?.message || 'Identifiants invalides');
        this.loading = false;
      }
    });
  }
}
