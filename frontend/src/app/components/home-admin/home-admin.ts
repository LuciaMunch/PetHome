import { Component } from '@angular/core';
import { AdminAnimales } from '../admin-animales/admin-animales';
import { AdminSanitario } from '../admin-sanitario/admin-sanitario';
import { AdminSolicitudes } from '../admin-solicitudes/admin-solicitudes';
import { AdminEstadisticas } from '../admin-estadisticas/admin-estadisticas';
import { AdminUsuarios } from '../admin-usuarios/admin-usuarios';

export interface NavItem {
  id: string;
  label: string;
  icon: string;
}

@Component({
  selector: 'app-home-admin',
  imports: [AdminAnimales, AdminSanitario, AdminSolicitudes, AdminEstadisticas, AdminUsuarios],
  templateUrl: './home-admin.html',
  styleUrl: './home-admin.css',
})
export class HomeAdmin {
  seccionActiva = 'mascotas';

  secciones: NavItem[] = [
    { id: 'mascotas', label: 'Mascotas', icon: '🐾' },
    { id: 'sanitario', label: 'Sanitario', icon: '💉' },
    { id: 'usuarios', label: 'Usuarios', icon: '👥' },
    { id: 'estadisticas', label: 'Estadísticas', icon: '📊' },
    { id: 'solicitudes', label: 'Solicitudes de adopción', icon: '📋' },
  ];

  seleccionar(id: string): void {
    this.seccionActiva = id;
  }
}
