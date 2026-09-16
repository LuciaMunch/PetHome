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

  cerrarSesion(): void {
    this.authService.logout();
    this.router.navigate(['/catalogo']);
  }
}
