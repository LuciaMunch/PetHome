import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuariosService, UsuarioResponse } from '../../services/usuario.service';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.css',
})
export class AdminUsuarios implements OnInit {
  usuarios = signal<UsuarioResponse[]>([]);
  cargando = signal(true);
  error = signal('');

  // Buscador por nombre de usuario o email
  buscar = signal('');

  usuariosFiltrados = computed(() => {
    const texto = this.buscar().toLowerCase().trim();
    if (!texto) return this.usuarios();
    return this.usuarios().filter(u =>
      u.nombreUsuario.toLowerCase().includes(texto) ||
      u.email.toLowerCase().includes(texto)
    );
  });

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
