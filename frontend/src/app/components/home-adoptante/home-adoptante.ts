import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuariosService, PerfilResponse, PerfilRequest } from '../../services/usuario.service';
import { SolicitudAdopcionService, SolicitudAdopcionResponse } from '../../services/solicitud-adopcion.service';

@Component({
  selector: 'app-home-adoptante',
  imports: [CommonModule, FormsModule],
  templateUrl: './home-adoptante.html',
  styleUrl: './home-adoptante.css',
})
export class HomeAdoptante implements OnInit {

  perfil = signal<PerfilResponse | null>(null);
  solicitudes = signal<SolicitudAdopcionResponse[]>([]);

  cargandoPerfil = signal(true);
  cargandoSolicitudes = signal(true);
  errorPerfil = signal('');
  errorSolicitudes = signal('');

  editando = signal(false);
  guardando = signal(false);
  mensajeEdicion = signal('');

  formulario = {
    direccion: '',
    ciudad: '',
    provincia: '',
    telefono: '',
    edad: null as number | null,
  };

  constructor(
    private usuariosService: UsuariosService,
    private solicitudService: SolicitudAdopcionService
  ) {}

  ngOnInit(): void {
    this.usuariosService.obtenerMiPerfil().subscribe({
      next: (data) => {
        this.perfil.set(data);
        this.cargandoPerfil.set(false);
      },
      error: () => {
        this.errorPerfil.set('No se pudo cargar tu perfil.');
        this.cargandoPerfil.set(false);
      }
    });

    this.solicitudService.misSolicitudes().subscribe({
      next: (data) => {
        this.solicitudes.set(data);
        this.cargandoSolicitudes.set(false);
      },
      error: () => {
        this.errorSolicitudes.set('No se pudieron cargar tus solicitudes.');
        this.cargandoSolicitudes.set(false);
      }
    });
  }

  iniciarEdicion(): void {
    const p = this.perfil();
    this.formulario.direccion = p?.direccion ?? '';
    this.formulario.ciudad = p?.ciudad ?? '';
    this.formulario.provincia = p?.provincia ?? '';
    this.formulario.telefono = p?.telefono ?? '';
    this.formulario.edad = p?.edad ?? null;
    this.mensajeEdicion.set('');
    this.editando.set(true);
  }

  cancelarEdicion(): void {
    this.editando.set(false);
    this.mensajeEdicion.set('');
  }

  guardar(): void {
    this.guardando.set(true);
    this.mensajeEdicion.set('');

    const data: PerfilRequest = {
      direccion: this.formulario.direccion,
      ciudad: this.formulario.ciudad,
      provincia: this.formulario.provincia,
      telefono: this.formulario.telefono,
      edad: this.formulario.edad,
    };

    this.usuariosService.actualizarMiPerfil(data).subscribe({
      next: (perfilActualizado) => {
        this.perfil.set(perfilActualizado);
        this.guardando.set(false);
        this.editando.set(false);
        this.mensajeEdicion.set('¡Perfil actualizado!');
      },
      error: () => {
        this.guardando.set(false);
        this.mensajeEdicion.set('No se pudo actualizar tu perfil. Intentalo de nuevo.');
      }
    });
  }
}
