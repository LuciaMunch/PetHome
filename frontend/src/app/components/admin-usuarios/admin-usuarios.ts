import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuariosService, UsuarioResponse } from '../../services/usuario.service';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.css',
})
export class AdminUsuarios implements OnInit {
  usuarios = signal<UsuarioResponse[]>([]);
  cargando = signal(true);
  error = signal('');

  constructor(private usuariosService: UsuariosService) {}

  ngOnInit(): void {
    this.usuariosService.listarAdoptantes().subscribe({
      next: (data) => {
        this.usuarios.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los usuarios.');
        this.cargando.set(false);
      }
    });
  }
}
