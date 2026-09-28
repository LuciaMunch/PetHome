import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EstadisticasService, EstadisticasResponse } from '../../services/estadisticas.service';

const NOMBRES_MES = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

interface Tarjeta { etiqueta: string; valor: number; icono: string; }
interface Categoria { nombre: string; cantidad: number; color: string; }
interface Progreso { etiqueta: string; valor: number; texto: string; }

@Component({
  selector: 'app-admin-estadisticas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-estadisticas.html',
  styleUrl: './admin-estadisticas.css',
})
export class AdminEstadisticas implements OnInit {
  cargando = signal(true);
  error = signal('');

  tarjetas = signal<Tarjeta[]>([]);
  estados = signal<Categoria[]>([]);
  especies = signal<Categoria[]>([]);
  tamanios = signal<Categoria[]>([]);
  adopcionesMes = signal<{ mes: string; cantidad: number }[]>([]);
  solicitudesResueltas = signal<Categoria[]>([]);
  progresos = signal<Progreso[]>([]);

  constructor(private estadisticasService: EstadisticasService) {}

  ngOnInit(): void {
    this.estadisticasService.obtenerResumen().subscribe({
      next: (data) => {
        try {
          this.armar(data);
        } catch (e) {
          console.error('Estadisticas: error en armar', e);
          this.error.set('Error al procesar: ' + e);
        } finally {
          this.cargando.set(false);
        }
      },
      error: (err) => {
        console.error('Estadisticas: error del request', err);
        this.error.set('No se pudieron cargar las estadísticas.');
        this.cargando.set(false);
      }
    });
  }

  private armar(d: EstadisticasResponse): void {
    this.tarjetas.set([
      { etiqueta: 'Adopciones completadas', valor: d.adoptados, icono: '🏡' },
      { etiqueta: 'Mascotas registradas', valor: d.totalAnimales, icono: '🐾' },
      { etiqueta: 'Solicitudes pendientes', valor: d.solicitudesPendientes, icono: '📋' },
      { etiqueta: 'Adoptantes registrados', valor: d.totalAdoptantes, icono: '👥' },
    ]);

    this.estados.set([
      { nombre: 'Disponibles', cantidad: d.animalesPorEstado['DISPONIBLE'] ?? 0, color: '#58A787' },
      { nombre: 'En proceso', cantidad: d.animalesPorEstado['EN_PROCESO'] ?? 0, color: '#FFB031' },
      { nombre: 'Adoptadas', cantidad: d.animalesPorEstado['ADOPTADO'] ?? 0, color: '#012312' },
    ]);

    this.especies.set([
      { nombre: 'Perros', cantidad: d.animalesPorEspecie['PERRO'] ?? 0, color: '#58A787' },
      { nombre: 'Gatos', cantidad: d.animalesPorEspecie['GATO'] ?? 0, color: '#FFB031' },
    ]);

    this.tamanios.set([
      { nombre: 'Pequeño', cantidad: d.animalesPorTamanio['PEQUENIO'] ?? 0, color: '#58A787' },
      { nombre: 'Mediano', cantidad: d.animalesPorTamanio['MEDIANO'] ?? 0, color: '#FFB031' },
      { nombre: 'Grande', cantidad: d.animalesPorTamanio['GRANDE'] ?? 0, color: '#FFD581' },
    ]);

    this.adopcionesMes.set(
      Object.entries(d.adopcionesPorMes)
        .sort((a, b) => a[0].localeCompare(b[0]))
        .map(([clave, cantidad]) => {
          const mes = Number(clave.split('-')[1]);
          return { mes: NOMBRES_MES[mes - 1] ?? clave, cantidad };
        })
    );

    this.solicitudesResueltas.set([
      { nombre: 'Aprobadas', cantidad: d.solicitudesAprobadas, color: '#58A787' },
      { nombre: 'Rechazadas', cantidad: d.solicitudesRechazadas, color: '#d32f2f' },
    ]);

    const conversion = this.porcentaje(d.solicitudesAprobadas, d.solicitudesAprobadas + d.solicitudesRechazadas);
    const adoptadas = this.porcentaje(d.adoptados, d.totalAnimales);

    this.progresos.set([
      { etiqueta: 'Tasa de conversión de solicitudes', valor: conversion, texto: conversion + '%' },
      { etiqueta: 'Mascotas adoptadas', valor: adoptadas, texto: adoptadas + '%' },
    ]);

    this.cargando.set(false);
  }

  private porcentaje(parte: number, total: number): number {
    return total > 0 ? Math.round((parte / total) * 100) : 0;
  }

  ancho(cantidad: number, total: number): number {
    return total > 0 ? (cantidad / total) * 100 : 0;
  }

  total(lista: { cantidad: number }[]): number {
    return lista.reduce((acc, item) => acc + item.cantidad, 0);
  }

  get maxMes(): number {
    return this.adopcionesMes().reduce((max, item) => Math.max(max, item.cantidad), 1);
  }
}
