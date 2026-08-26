import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SolicitudAdopcionService } from '../../services/solicitud-adopcion.service';

interface SolicitudAdmin {
  id: number;
  animalNombre: string;
  solicitanteEmail: string;
  tipoVivienda: string;
  tienePatio: boolean;
  motivo: string;
  estado: 'PENDIENTE' | 'APROBADA' | 'RECHAZADA';
  fechaCreacion: string;
}

@Component({
  selector: 'app-admin-solicitudes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-solicitudes.html',
  styleUrl: './admin-solicitudes.css',
})
export class AdminSolicitudes implements OnInit {

  solicitudes: SolicitudAdmin[] = [];
  cargando = true;

  constructor(private solicitudService: SolicitudAdopcionService) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.solicitudService.obtenerTodas().subscribe({
      next: (data) => {
        this.solicitudes = data;
        this.cargando = false;
      },
      error: () => {
        // Fallback / Datos MOCK por si no hay conexión al backend
        this.solicitudes = [
          { id: 101, animalNombre: 'Luna', solicitanteEmail: 'adoptante1@gmail.com', tipoVivienda: 'CASA', tienePatio: true, motivo: 'Tengo espacio y ganas de cuidar una mascota.', estado: 'PENDIENTE', fechaCreacion: '2026-05-12' },
          { id: 102, animalNombre: 'Michi', solicitanteEmail: 'adoptante2@gmail.com', tipoVivienda: 'DEPARTAMENTO', tienePatio: false, motivo: 'Trabajo desde casa y busco compañía.', estado: 'PENDIENTE', fechaCreacion: '2026-05-11' }
        ];
        this.cargando = false;
      }
    });
  }

  cambiarEstado(id: number, nuevoEstado: 'APROBADA' | 'RECHAZADA'): void {
    this.solicitudService.actualizarEstado(id, nuevoEstado).subscribe({
      next: () => this.cargarSolicitudes(),
      error: () => {
        // Actualización local en pantalla
        const item = this.solicitudes.find(s => s.id === id);
        if (item) item.estado = nuevoEstado;
      }
    });
  }
}
