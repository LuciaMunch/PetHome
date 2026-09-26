import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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

  solicitudSeleccionada: SolicitudAdopcionResponse | null = null;

  constructor(
    private solicitudService: SolicitudAdopcionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.cargando = true;
    this.solicitudService.obtenerPendientes().subscribe({
      next: (data) => {
        this.solicitudes = data.content;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar las solicitudes.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  verDetalle(solicitud: SolicitudAdopcionResponse): void {
    this.solicitudSeleccionada = solicitud;
  }

  cerrarDetalle(): void {
    this.solicitudSeleccionada = null;
  }

  aprobar(id: number): void {
    this.solicitudService.aprobar(id).subscribe({
      next: () => {
        this.cerrarDetalle();
        this.cargarSolicitudes();
      },
      error: () => {
        this.error = 'No se pudo aprobar la solicitud.';
        this.cdr.detectChanges();
      }
    });
  }

  rechazar(id: number): void {
    this.solicitudService.rechazar(id).subscribe({
      next: () => {
        this.cerrarDetalle();
        this.cargarSolicitudes();
      },
      error: () => {
        this.error = 'No se pudo rechazar la solicitud.';
        this.cdr.detectChanges();
      }
    });
  }
}
