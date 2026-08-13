import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
// import { ModalFormularioAdopcion } from '../modal-formulario-adopcion/modal-formulario-adopcion';

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
  imports: [CommonModule, RouterLink],
  templateUrl: './ficha-animal.html',
  styleUrl: './ficha-animal.css',
})
export class FichaAnimal implements OnInit {

  animalId!: number;

  // Datos de ejemplo (después vendrá del backend según el id)
  animal: Animal = {
    id: 1, nombre: 'Luna', especie: 'PERRO', sexo: 'HEMBRA', tamanio: 'MEDIANO',
    edad: 2, descripcion: 'Le encanta jugar en el patio y es muy sociable con otros perros. Ideal para una familia con espacio al aire libre.',
    estado: 'DISPONIBLE'
  };

  // Galería de ejemplo (después vendrá de tu endpoint de fotos)
  fotos: string[] = [];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Lee el id de la URL (/animal/5 -> animalId = 5)
    this.animalId = Number(this.route.snapshot.paramMap.get('id'));
    // Acá después se llamaría al backend para traer el animal y sus fotos
  }

  // Controla si el modal de adopción está abierto
  mostrarModalAdopcion = false;

  abrirAdopcion(): void {
    this.mostrarModalAdopcion = true;
  }

  cerrarAdopcion(): void {
    this.mostrarModalAdopcion = false;
  }
}
