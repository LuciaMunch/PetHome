import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-auth',
  imports: [ReactiveFormsModule],
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class Auth {
  modo: 'login' | 'registro' = 'login';
  error = '';
  loading = false;

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
        this.form = this.fb.group({
          nombre_usuario: ['', Validators.required],
          contraseña: ['', Validators.required],
          email: ['', this.modo === 'registro' ? Validators.required : Validators.nullValidator]
        });
  }

  cambiarModo(): void {
    this.modo = this.modo === 'login' ? 'registro' : 'login';
    this.error = '';
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';

    const { nombre_usuario, contraseña, email } = this.form.value;

    const request = this.modo === 'login'
      ? this.authService.login(nombre_usuario, contraseña)
      : this.authService.register({ nombre_usuario, contraseña, email, rol: 'ADOPTANTE' });

    request.subscribe({
      next: (res) => {
        const destino = res.rol === 'ADMIN' ? '/home-admin' : '/home-adoptante';
        this.router.navigate([destino]);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error || 'Error en el servidor';
      }
    });
  }
}
