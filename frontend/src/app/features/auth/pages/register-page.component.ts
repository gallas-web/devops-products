import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzCheckboxModule } from 'ng-zorro-antd/checkbox';

import { AuthService } from '../../../core/services/auth.service';
import { RegisterRequest } from '../../../core/models/user.model';

@Component({
  selector: 'app-register',
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
    NzCheckboxModule,
    RouterLink
  ],
  template: `
    <div class="auth-container">
      <nz-card class="auth-card" [nzTitle]="'Créer un compte'">
        <nz-spin [nzSpinning]="loading">
          <form nz-form [formGroup]="form" nzLayout="vertical" (ngSubmit)="onSubmit()">
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px">
              <nz-form-item>
                <nz-form-label nzRequired>Prénom</nz-form-label>
                <nz-form-control nzErrorTip="Prénom obligatoire">
                  <input nz-input formControlName="firstName" placeholder="Votre prénom" />
                </nz-form-control>
              </nz-form-item>

              <nz-form-item>
                <nz-form-label nzRequired>Nom</nz-form-label>
                <nz-form-control nzErrorTip="Nom obligatoire">
                  <input nz-input formControlName="lastName" placeholder="Votre nom" />
                </nz-form-control>
              </nz-form-item>
            </div>

            <nz-form-item>
              <nz-form-label nzRequired>Email</nz-form-label>
              <nz-form-control nzErrorTip="Veuillez entrer un email valide">
                <input nz-input formControlName="email" placeholder="votre@email.com" />
              </nz-form-control>
            </nz-form-item>


            <nz-form-item>
              <nz-form-label nzRequired>Mot de passe</nz-form-label>
              <nz-form-control 
                [nzErrorTip]="(form.get('password')?.errors | json) ? 'Min 8 caractères avec majuscule, minuscule, chiffre' : ''"
              >
                <input nz-input type="password" formControlName="password" 
                       placeholder="Min 8 caractères" />
              </nz-form-control>
            </nz-form-item>


            <nz-form-item>
              <nz-form-label nzRequired>Confirmer mot de passe</nz-form-label>
              <nz-form-control nzErrorTip="Les mots de passe ne correspondent pas">
                <input nz-input type="password" formControlName="confirmPassword" 
                       placeholder="Confirmer le mot de passe" />
              </nz-form-control>
            </nz-form-item>

            <nz-form-item>
              <nz-form-control>
                <label nz-checkbox formControlName="terms">
                  <span>J'accepte les <a href="javascript:" style="color: #1890ff">conditions d'utilisation</a></span>
                </label>
              </nz-form-control>
            </nz-form-item>

            <nz-form-item>
              <button nz-button nzType="primary" nzBlock nzSize="large" type="submit" [disabled]="loading || !form.valid">
                S'inscrire
              </button>
            </nz-form-item>

            <div style="text-align: center; margin-top: 16px">
              <span>Vous avez déjà un compte? </span>
              <a routerLink="/auth/login" style="color: #1890ff">Se connecter</a>
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
      max-width: 500px;
      border-radius: 8px;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    }
    nz-form-item {
      margin-bottom: 16px;
    }
  `]
})
export class RegisterComponent implements OnInit {
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
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]],
      confirmPassword: ['', Validators.required],
      terms: [false, [Validators.requiredTrue]]
    }, { validators: this.passwordMatchValidator });
  }

  private passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;
    
    const hasUpper = /[A-Z]/.test(value);
    const hasLower = /[a-z]/.test(value);
    const hasDigit = /[0-9]/.test(value);
    
    if (!hasUpper || !hasLower || !hasDigit) {
      return { weakPassword: true };
    }
    return null;
  }

  private passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    
    if (password && confirmPassword && password !== confirmPassword) {
      group.get('confirmPassword')?.setErrors({ mismatch: true });
      return { mismatch: true };
    }
    return null;
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
    const request = {
      firstName: this.form.get('firstName')?.value,
      lastName: this.form.get('lastName')?.value,
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value
    } as RegisterRequest;

    this.authService.register(request).subscribe({
      next: () => {
        this.message.success('Inscription réussie! Bienvenue!');
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.message.error(err.error?.message || 'Erreur lors de l\'inscription');
        this.loading = false;
      }
    });
  }
}
