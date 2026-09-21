import { Component } from '@angular/core';
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

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  get rol(): string | null {
    return this.authService.getRol();
  }

  irAInicio(): void {
    // TODO: cuando Guada termine la landing, cambiar '/catalogo' por su ruta
    this.router.navigate(['/catalogo']);
  }

  cerrarSesion(): void {
    this.authService.logout();
    // TODO: cuando Guada termine la landing, cambiar '/catalogo' por su ruta
    this.router.navigate(['/catalogo']);
  }
}
