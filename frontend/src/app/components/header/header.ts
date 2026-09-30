import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

  menuAbierto = signal(false);

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  get rol(): string | null {
    return this.authService.getRol();
  }
 toggleMenu(): void {
    this.menuAbierto.update(v => !v);
  }

  cerrarMenu(): void {
    this.menuAbierto.set(false);
  }

  irAInicio(): void {
    this.router.navigate(['/']);
  }

  cerrarSesion(): void {
    this.authService.logout();
    this.menuAbierto.set(false);
    this.router.navigate(['/']);
  }
}
