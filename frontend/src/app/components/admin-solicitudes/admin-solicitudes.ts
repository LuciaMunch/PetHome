import { Component, OnInit } from '@angular/core';
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

  solicitudes: SolicitudAdopcionResponse[] = [];
  cargando = true;
  error = '';

  constructor(private solicitudService: SolicitudAdopcionService) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.cargando = true;
    this.solicitudService.obtenerPendientes().subscribe({
      next: (data) => {
        this.solicitudes = data.content;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las solicitudes.';
        this.cargando = false;
      }
    });
  }

  aprobar(id: number): void {
    this.solicitudService.aprobar(id).subscribe({
      next: () => this.cargarSolicitudes(),
      error: () => this.error = 'No se pudo aprobar la solicitud.'
    });
  }

  rechazar(id: number): void {
    this.solicitudService.rechazar(id).subscribe({
      next: () => this.cargarSolicitudes(),
      error: () => this.error = 'No se pudo rechazar la solicitud.'
    });
  }
}
