import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home-adoptante',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home-adoptante.html',
  styleUrl: './home-adoptante.css',
})
export class HomeAdoptante implements OnInit {

  nombreUsuario = '';
  rol = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.rol = this.authService.getRol() ?? '';
    this.nombreUsuario = this.obtenerNombreUsuarioDelToken();
  }

  private obtenerNombreUsuarioDelToken(): string {
    const token = this.authService.getToken();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub ?? '';
    } catch {
      return '';
    }
  }
}
