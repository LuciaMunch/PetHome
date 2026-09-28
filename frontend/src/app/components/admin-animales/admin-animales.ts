import { Component, OnInit, signal } from '@angular/core';
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

  animales = signal<Animal[]>([]);

  // Datos de ejemplo (plan B, si el backend no responde)
  private ejemplo: Animal[] = [
    { id: 1, nombre: 'Luna',  especie: 'PERRO', sexo: 'HEMBRA', tamanio: 'MEDIANO',  edad: 2, descripcion: 'Le encanta jugar en el patio', estado: 'DISPONIBLE' },
    { id: 2, nombre: 'Michi', especie: 'GATO',  sexo: 'MACHO',  tamanio: 'PEQUENIO', edad: 1, descripcion: 'Muy cariñoso', estado: 'EN_PROCESO' },
    { id: 3, nombre: 'Rocky', especie: 'PERRO', sexo: 'MACHO',  tamanio: 'GRANDE',   edad: 4, descripcion: 'Ya encontró su hogar', estado: 'ADOPTADO' },
  ];

  mostrarFormulario = signal(false);
  animalActual: Animal = this.animalVacio();
  fotosSeleccionadas = signal<string[]>([]);
  private archivoFoto: File | null = null;

  constructor(private animalService: AnimalService) {}

  ngOnInit(): void {
    this.cargarAnimales();
  }

  // Trae la lista de animales del backend (si falla, usa los de ejemplo)
  private cargarAnimales(): void {
    this.animalService.listarTodos().subscribe({
      next: (data) => this.animales.set(data),
      error: () => this.animales.set([...this.ejemplo])
    });
  }

  private animalVacio(): Animal {
    return { id: 0, nombre: '', especie: '', sexo: '', tamanio: '', edad: 0, descripcion: '', estado: 'DISPONIBLE' };
  }

  nuevo(): void {
    this.animalActual = this.animalVacio();
    this.fotosSeleccionadas.set([]);
    this.archivoFoto = null;
    this.mostrarFormulario.set(true);
  }

  editar(animal: Animal): void {
    this.animalActual = { ...animal };
    this.fotosSeleccionadas.set([]);
    this.archivoFoto = null;
    this.mostrarFormulario.set(true);
  }

  guardar(): void {
    if (this.animalActual.id === 0) {
      // Crear
      this.animalService.crear(this.animalActual).subscribe({
        next: (creado) => {
          this.animales.update(lista => [...lista, creado]);
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
          this.animales.update(lista => [...lista, this.animalActual]);
          this.cerrarFormulario();
        }
      });
    } else {
      // Editar
      this.animalService.actualizar(this.animalActual.id, this.animalActual).subscribe({
        next: (actualizado) => {
          this.animales.update(lista => lista.map(a => a.id === actualizado.id ? actualizado : a));
          this.cerrarFormulario();
        },
        error: () => {
          this.animales.update(lista => lista.map(a => a.id === this.animalActual.id ? this.animalActual : a));
          this.cerrarFormulario();
        }
      });
    }
  }

  borrar(animal: Animal): void {
    if (!confirm('¿Seguro que querés borrar a ' + animal.nombre + '?')) return;
    this.animalService.eliminar(animal.id).subscribe({
      next: () => this.animales.update(lista => lista.filter(a => a.id !== animal.id)),
      error: () => this.animales.update(lista => lista.filter(a => a.id !== animal.id))
    });
  }

  cerrarFormulario(): void {
    this.mostrarFormulario.set(false);
  }

  onFotoSeleccionada(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.archivoFoto = input.files[0];
      this.fotosSeleccionadas.update(lista => [...lista, input.files![0].name]);
    }
  }

  quitarFoto(nombre: string): void {
    this.fotosSeleccionadas.update(lista => lista.filter(f => f !== nombre));
    this.archivoFoto = null;
  }
}
