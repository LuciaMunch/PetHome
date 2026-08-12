import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'token';
  private readonly ROLE_KEY = 'rol';

  readonly isAuthenticated = signal(this.getToken() !== null);
  readonly rol = signal(this.getRol());

  constructor(private http: HttpClient, private router: Router) {}

  login(nombre_usuario: string, contraseña: string): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}/login`, { nombre_usuario, contraseña })
      .pipe(tap((res) => this.saveSession(res)));
  }

  register(data: { nombre_usuario: string; contraseña: string; email: string; rol: string }): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}/register`, data)
      .pipe(tap((res) => this.saveSession(res)));
  }

  private saveSession(res: any): void {
    localStorage.setItem(this.TOKEN_KEY, res.token);
    localStorage.setItem(this.ROLE_KEY, res.rol);
    this.isAuthenticated.set(true);
    this.rol.set(res.rol);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRol(): string | null {
    return localStorage.getItem(this.ROLE_KEY);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    this.isAuthenticated.set(false);
    this.rol.set(null);
    this.router.navigate(['/auth']);
  }
}
