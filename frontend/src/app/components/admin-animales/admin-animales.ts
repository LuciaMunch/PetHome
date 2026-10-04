import { Component, OnInit, signal, computed } from '@angular/core';
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

  // Filtros de búsqueda
  buscarNombre = signal('');
  filtroEspecie = signal('');
  filtroSexo = signal('');
  filtroEstado = signal('');

  // Lista filtrada: se recalcula sola cuando cambia la lista o algún filtro
  animalesFiltrados = computed(() => {
    const nombre = this.buscarNombre().toLowerCase().trim();
    const especie = this.filtroEspecie();
    const sexo = this.filtroSexo();
    const estado = this.filtroEstado();

    return this.animales().filter(a =>
      (!nombre || a.nombre.toLowerCase().includes(nombre)) &&
      (!especie || a.especie === especie) &&
      (!sexo || a.sexo === sexo) &&
      (!estado || a.estado === estado)
    );
  });

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

  // Confirmación de borrado (reemplaza al confirm() nativo del navegador)
  mostrarConfirmacionBorrar = signal(false);
  animalABorrar: Animal | null = null;

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

  limpiarFiltros(): void {
    this.buscarNombre.set('');
    this.filtroEspecie.set('');
    this.filtroSexo.set('');
    this.filtroEstado.set('');
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
              next: () => { console.log('Foto subida'); this.cargarAnimales(); },
              error: () => console.log('No se pudo subir la foto')
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
          // Si hay una foto seleccionada, la subimos al animal editado
          if (this.archivoFoto) {
            this.animalService.subirFoto(actualizado.id, this.archivoFoto).subscribe({
              next: () => { console.log('Foto subida'); this.cargarAnimales(); },
              error: () => console.log('No se pudo subir la foto')
            });
          }
          this.cerrarFormulario();
        },
        error: () => {
          this.animales.update(lista => lista.map(a => a.id === this.animalActual.id ? this.animalActual : a));
          this.cerrarFormulario();
        }
      });
    }
  }

  // Abre el modal de confirmación en vez del confirm() nativo
  borrar(animal: Animal): void {
    this.animalABorrar = animal;
    this.mostrarConfirmacionBorrar.set(true);
  }

  // Se ejecuta cuando el usuario confirma el borrado en el modal
  confirmarBorrado(): void {
    if (!this.animalABorrar) return;
    const animal = this.animalABorrar;

    this.animalService.eliminar(animal.id).subscribe({
      next: () => this.animales.update(lista => lista.filter(a => a.id !== animal.id)),
      error: () => this.animales.update(lista => lista.filter(a => a.id !== animal.id))
    });

    this.cancelarBorrado();
  }

  // Cierra el modal sin borrar nada
  cancelarBorrado(): void {
    this.mostrarConfirmacionBorrar.set(false);
    this.animalABorrar = null;
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
