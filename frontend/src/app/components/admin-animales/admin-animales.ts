import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

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
export class AdminAnimales {

  animales: Animal[] = [
    { id: 1, nombre: 'Luna',  especie: 'PERRO', sexo: 'HEMBRA', tamanio: 'MEDIANO',  edad: 2, descripcion: 'Le encanta jugar en el patio', estado: 'DISPONIBLE' },
    { id: 2, nombre: 'Michi', especie: 'GATO',  sexo: 'MACHO',  tamanio: 'PEQUENIO', edad: 1, descripcion: 'Muy cariñoso', estado: 'EN_PROCESO' },
    { id: 3, nombre: 'Rocky', especie: 'PERRO', sexo: 'MACHO',  tamanio: 'GRANDE',   edad: 4, descripcion: 'Ya encontró su hogar', estado: 'ADOPTADO' },
  ];

  // Controla si el formulario está abierto
  mostrarFormulario = false;

  // El animal que se está creando o editando
  animalActual: Animal = this.animalVacio();

  // Crea un objeto animal vacío (para el alta)
  private animalVacio(): Animal {
    return { id: 0, nombre: '', especie: '', sexo: '', tamanio: '', edad: 0, descripcion: '', estado: 'DISPONIBLE' };
  }

  // Abre el formulario vacío para crear
  nuevo(): void {
    this.animalActual = this.animalVacio();
    this.fotosSeleccionadas = [];
    this.mostrarFormulario = true;
  }

  editar(animal: Animal): void {
    this.animalActual = { ...animal };
    this.fotosSeleccionadas = [];
    this.mostrarFormulario = true;
  }

  // Guarda (crea o actualiza según si tiene id)
  guardar(): void {
    if (this.animalActual.id === 0) {
      // Crear: le asigna un id nuevo y lo agrega
      this.animalActual.id = Date.now();
      this.animales.push(this.animalActual);
    } else {
      // Editar: reemplaza el existente
      const i = this.animales.findIndex(a => a.id === this.animalActual.id);
      if (i !== -1) this.animales[i] = this.animalActual;
    }
    this.cerrarFormulario();
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
  }

  borrar(animal: Animal): void {
    if (confirm('¿Seguro que querés borrar a ' + animal.nombre + '?')) {
      this.animales = this.animales.filter(a => a.id !== animal.id);
    }
  }

  // Fotos seleccionadas para el animal actual (por ahora, solo los nombres de archivo)
  fotosSeleccionadas: string[] = [];

  // Se ejecuta al elegir un archivo
  onFotoSeleccionada(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const archivo = input.files[0];
      this.fotosSeleccionadas.push(archivo.name);
      // (después: acá se subiría el archivo a Cloudinary vía tu endpoint POST /api/animales/{id}/fotos)
    }
  }

  // Saca una foto de la lista
  quitarFoto(nombre: string): void {
    this.fotosSeleccionadas = this.fotosSeleccionadas.filter(f => f !== nombre);
  }
}
