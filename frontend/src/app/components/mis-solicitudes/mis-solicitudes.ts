import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SolicitudAdopcionService, SolicitudAdopcionResponse } from '../../services/solicitud-adopcion.service';

@Component({
  selector: 'app-mis-solicitudes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mis-solicitudes.html',
  styleUrl: './mis-solicitudes.css',
})
export class MisSolicitudes implements OnInit {

  solicitudes: SolicitudAdopcionResponse[] = [];
  cargando = true;
  error = '';

  constructor(private solicitudService: SolicitudAdopcionService) {}

  ngOnInit(): void {
    this.solicitudService.misSolicitudes().subscribe({
      next: (data) => {
        this.solicitudes = data;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar tus solicitudes.';
        this.cargando = false;
      }
    });
  }
}
