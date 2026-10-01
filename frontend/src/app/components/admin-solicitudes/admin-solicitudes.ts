import { Component, OnInit, ChangeDetectorRef, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SolicitudAdopcionService, SolicitudAdopcionResponse } from '../../services/solicitud-adopcion.service';

@Component({
  selector: 'app-admin-solicitudes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-solicitudes.html',
  styleUrl: './admin-solicitudes.css',
})
export class AdminSolicitudes implements OnInit {

  solicitudes = signal<SolicitudAdopcionResponse[]>([]);
  cargando = true;
  error = '';

  // Buscador por nombre de adoptante o animal
  buscar = signal('');

  solicitudesFiltradas = computed(() => {
    const texto = this.buscar().toLowerCase().trim();
    if (!texto) return this.solicitudes();
    return this.solicitudes().filter(s =>
      s.usuarioNombre.toLowerCase().includes(texto) ||
      s.animalNombre.toLowerCase().includes(texto)
    );
  });

  solicitudSeleccionada: SolicitudAdopcionResponse | null = null;

  // Cartel de confirmación (notificación al adoptante)
  mensajeConfirmacion = '';

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
        this.solicitudes.set(data.content);
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

  cerrarConfirmacion(): void {
    this.mensajeConfirmacion = '';
  }

  aprobar(id: number): void {
    const sol = this.solicitudSeleccionada;
    this.solicitudService.aprobar(id).subscribe({
      next: () => {
        this.cerrarDetalle();
        if (sol) {
          this.mensajeConfirmacion = `Solicitud aprobada. Se notificó a ${sol.usuarioNombre} por correo a ${sol.usuarioEmail}.`;
        }
        this.cargarSolicitudes();
      },
      error: () => {
        this.error = 'No se pudo aprobar la solicitud.';
        this.cdr.detectChanges();
      }
    });
  }

  rechazar(id: number): void {
    const sol = this.solicitudSeleccionada;
    this.solicitudService.rechazar(id).subscribe({
      next: () => {
        this.cerrarDetalle();
        if (sol) {
          this.mensajeConfirmacion = `Solicitud rechazada. Se notificó a ${sol.usuarioNombre} por correo a ${sol.usuarioEmail}.`;
        }
        this.cargarSolicitudes();
      },
      error: () => {
        this.error = 'No se pudo rechazar la solicitud.';
        this.cdr.detectChanges();
      }
    });
  }
}
