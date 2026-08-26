import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnimalService } from '../../services/animal.service';

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
  selector: 'app-admin-animales',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-animales.html',
  styleUrl: './admin-animales.css',
})
export class AdminAnimales implements OnInit {

  animales: Animal[] = [];

  // Datos de ejemplo (plan B, si el backend no responde)
  private ejemplo: Animal[] = [
    { id: 1, nombre: 'Luna',  especie: 'PERRO', sexo: 'HEMBRA', tamanio: 'MEDIANO',  edad: 2, descripcion: 'Le encanta jugar en el patio', estado: 'DISPONIBLE' },
    { id: 2, nombre: 'Michi', especie: 'GATO',  sexo: 'MACHO',  tamanio: 'PEQUENIO', edad: 1, descripcion: 'Muy cariñoso', estado: 'EN_PROCESO' },
    { id: 3, nombre: 'Rocky', especie: 'PERRO', sexo: 'MACHO',  tamanio: 'GRANDE',   edad: 4, descripcion: 'Ya encontró su hogar', estado: 'ADOPTADO' },
  ];

  mostrarFormulario = false;
  animalActual: Animal = this.animalVacio();
  fotosSeleccionadas: string[] = [];
  private archivoFoto: File | null = null;

  constructor(private animalService: AnimalService) {}

  ngOnInit(): void {
    this.cargarAnimales();
  }

  // Trae la lista de animales del backend (si falla, usa los de ejemplo)
  private cargarAnimales(): void {
    this.animalService.listarTodos().subscribe({
      next: (data) => this.animales = data,
      error: () => this.animales = [...this.ejemplo]
    });
  }

  private animalVacio(): Animal {
    return { id: 0, nombre: '', especie: '', sexo: '', tamanio: '', edad: 0, descripcion: '', estado: 'DISPONIBLE' };
  }

  nuevo(): void {
    this.animalActual = this.animalVacio();
    this.fotosSeleccionadas = [];
    this.archivoFoto = null;
    this.mostrarFormulario = true;
  }

  editar(animal: Animal): void {
    this.animalActual = { ...animal };
    this.fotosSeleccionadas = [];
    this.archivoFoto = null;
    this.mostrarFormulario = true;
  }

  guardar(): void {
    if (this.animalActual.id === 0) {
      // Crear
      this.animalService.crear(this.animalActual).subscribe({
        next: (creado) => {
          this.animales.push(creado);
          // Si hay una foto seleccionada, la subimos al animal recién creado
          if (this.archivoFoto) {
            this.animalService.subirFoto(creado.id, this.archivoFoto).subscribe({
              next: () => console.log('Foto subida'),
              error: () => console.log('No se pudo subir la foto (login pendiente)')
            });
          }
          this.cerrarFormulario();
        },
        error: () => {
          this.animalActual.id = Date.now();
          this.animales.push(this.animalActual);
          this.cerrarFormulario();
        }
      });
    } else {
      // Editar
      this.animalService.actualizar(this.animalActual.id, this.animalActual).subscribe({
        next: (actualizado) => {
          const i = this.animales.findIndex(a => a.id === actualizado.id);
          if (i !== -1) this.animales[i] = actualizado;
          this.cerrarFormulario();
        },
        error: () => {
          const i = this.animales.findIndex(a => a.id === this.animalActual.id);
          if (i !== -1) this.animales[i] = this.animalActual;
          this.cerrarFormulario();
        }
      });
    }
  }

  borrar(animal: Animal): void {
    if (!confirm('¿Seguro que querés borrar a ' + animal.nombre + '?')) return;
    this.animalService.eliminar(animal.id).subscribe({
      next: () => this.animales = this.animales.filter(a => a.id !== animal.id),
      error: () => this.animales = this.animales.filter(a => a.id !== animal.id)
    });
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
  }

  onFotoSeleccionada(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.archivoFoto = input.files[0];
      this.fotosSeleccionadas.push(input.files[0].name);
    }
  }

  quitarFoto(nombre: string): void {
    this.fotosSeleccionadas = this.fotosSeleccionadas.filter(f => f !== nombre);
    this.archivoFoto = null;
  }
}
