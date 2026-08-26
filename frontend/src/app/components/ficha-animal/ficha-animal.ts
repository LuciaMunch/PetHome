import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AnimalService } from '../../services/animal.service';
import { ModalFormularioAdopcion } from '../modal-formulario-adopcion/modal-formulario-adopcion';

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
  selector: 'app-ficha-animal',
  standalone: true,
  imports: [CommonModule, RouterLink, ModalFormularioAdopcion],
  templateUrl: './ficha-animal.html',
  styleUrl: './ficha-animal.css',
})
export class FichaAnimal implements OnInit {

  animalId!: number;
  animal!: Animal;
  fotos: string[] = [];

  private ejemplo: Animal[] = [
    { id: 1, nombre: 'Luna',  especie: 'PERRO', sexo: 'HEMBRA', tamanio: 'MEDIANO',  edad: 2, descripcion: 'Le encanta jugar en el patio y es muy sociable con otros perros.', estado: 'DISPONIBLE' },
    { id: 2, nombre: 'Michi', especie: 'GATO',  sexo: 'MACHO',  tamanio: 'PEQUENIO', edad: 1, descripcion: 'Muy cariñoso, ideal para departamento.', estado: 'EN_PROCESO' },
    { id: 3, nombre: 'Rocky', especie: 'PERRO', sexo: 'MACHO',  tamanio: 'GRANDE',   edad: 4, descripcion: 'Ya encontró su hogar. Un perro noble y tranquilo.', estado: 'ADOPTADO' },
    { id: 4, nombre: 'Coco',  especie: 'GATO',  sexo: 'HEMBRA', tamanio: 'MEDIANO',  edad: 3, descripcion: 'Tranquila, se lleva bien con niños.', estado: 'DISPONIBLE' },
  ];

  mostrarModalAdopcion = false;

  constructor(
    private route: ActivatedRoute,
    private animalService: AnimalService
  ) {}

  ngOnInit(): void {
    this.animalId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarAnimal();
    this.cargarFotos();
  }

  private cargarAnimal(): void {
    this.animalService.obtenerPorId(this.animalId).subscribe({
      next: (data) => this.animal = data,
      error: () => {
        this.animal = this.ejemplo.find(a => a.id === this.animalId) ?? this.ejemplo[0];
      }
    });
  }

  private cargarFotos(): void {
    this.animalService.obtenerFotos(this.animalId).subscribe({
      next: (data) => this.fotos = data.map((f: any) => f.url),
      error: () => this.fotos = []
    });
  }

  abrirAdopcion(): void {
    this.mostrarModalAdopcion = true;
  }

  cerrarAdopcion(): void {
    this.mostrarModalAdopcion = false;
  }
}
