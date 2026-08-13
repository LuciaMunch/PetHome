import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CatalogoService } from '../../services/catalogo.service';
import { RouterLink } from '@angular/router';

interface Animal {
  id: number;
  nombre: string;
  especie: string;
  sexo: string;
  tamanio: string;
  edad: number;
  descripcion: string;
  estado: string;
}

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.css',
})
export class Catalogo implements OnInit {

  // Datos de ejemplo (plan B, si el backend no responde todavía)
  private ejemplo: Animal[] = [
    { id: 1, nombre: 'Luna',  especie: 'PERRO', sexo: 'HEMBRA', tamanio: 'MEDIANO',  edad: 2, descripcion: 'Le encanta jugar en el patio', estado: 'DISPONIBLE' },
    { id: 2, nombre: 'Michi', especie: 'GATO',  sexo: 'MACHO',  tamanio: 'PEQUENIO', edad: 1, descripcion: 'Muy cariñoso, ideal para departamento', estado: 'EN_PROCESO' },
    { id: 3, nombre: 'Rocky', especie: 'PERRO', sexo: 'MACHO',  tamanio: 'GRANDE',   edad: 4, descripcion: 'Ya encontró su hogar', estado: 'ADOPTADO' },
    { id: 4, nombre: 'Coco',  especie: 'GATO',  sexo: 'HEMBRA', tamanio: 'MEDIANO',  edad: 3, descripcion: 'Tranquila, se lleva bien con niños', estado: 'DISPONIBLE' },
  ];

  animales: Animal[] = [];

  filtroEspecie = '';
  filtroSexo = '';
  filtroTamanio = '';
  filtroEdadMax: number | null = null;

  constructor(private catalogoService: CatalogoService) {}

  // Al arrancar el componente, trae los animales
  ngOnInit(): void {
    this.buscar();
  }

  buscar(): void {
    this.catalogoService.listar({
      especie: this.filtroEspecie || undefined,
      sexo: this.filtroSexo || undefined,
      tamanio: this.filtroTamanio || undefined,
      edadMax: this.filtroEdadMax ?? undefined,
    }).subscribe({
      next: (respuesta) => {
        // El backend devuelve una página; los animales están en "content"
        this.animales = respuesta.content ?? respuesta;
      },
      error: () => {
        // Si el backend no responde (403, apagado, etc.), usa los de ejemplo filtrados
        this.animales = this.filtrarEjemplo();
      }
    });
  }

  limpiar(): void {
    this.filtroEspecie = '';
    this.filtroSexo = '';
    this.filtroTamanio = '';
    this.filtroEdadMax = null;
    this.buscar();
  }

  // Filtra los datos de ejemplo localmente (plan B)
  private filtrarEjemplo(): Animal[] {
    return this.ejemplo.filter(a =>
      (!this.filtroEspecie || a.especie === this.filtroEspecie) &&
      (!this.filtroSexo     || a.sexo === this.filtroSexo) &&
      (!this.filtroTamanio  || a.tamanio === this.filtroTamanio) &&
      (this.filtroEdadMax == null || a.edad <= this.filtroEdadMax)
    );
  }
}
