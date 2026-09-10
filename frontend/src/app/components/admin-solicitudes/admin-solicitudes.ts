import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SolicitudAdopcionService, SolicitudAdopcionResponse } from '../../services/solicitud-adopcion.service';

@Component({
  selector: 'app-admin-solicitudes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-solicitudes.html',
  styleUrl: './admin-solicitudes.css',
})
export class AdminSolicitudes implements OnInit {

  solicitudes = signal<SolicitudAdopcionResponse[]>([]);
  cargando = signal(true);
  error = signal('');

  constructor(private solicitudService: SolicitudAdopcionService) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.cargando.set(true);
    this.error.set('');
    this.solicitudService.obtenerPendientes().subscribe({
      next: (data) => {
        this.solicitudes.set(data.content);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las solicitudes.');
        this.cargando.set(false);
      }
    });
  }

  aprobar(id: number): void {
    this.solicitudService.aprobar(id).subscribe({
      next: () => this.cargarSolicitudes(),
      error: () => this.error.set('No se pudo aprobar la solicitud.')
    });
  }

  rechazar(id: number): void {
    this.solicitudService.rechazar(id).subscribe({
      next: () => this.cargarSolicitudes(),
      error: () => this.error.set('No se pudo rechazar la solicitud.')
    });
  }
}
