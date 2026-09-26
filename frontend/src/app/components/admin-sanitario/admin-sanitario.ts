import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AnimalService } from '../../services/animal.service';
import { EventoSanitarioService, EventoSanitarioResponse } from '../../services/evento-sanitario.service';

interface AnimalOpcion {
  id: number;
  nombre: string;
}

@Component({
  selector: 'app-admin-sanitario',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-sanitario.html',
  styleUrl: './admin-sanitario.css',
})
export class AdminSanitario implements OnInit {

  animales = signal<AnimalOpcion[]>([]);
  animalSeleccionadoId: number | null = null;
  historial = signal<EventoSanitarioResponse[]>([]);

  cargandoAnimales = signal(true);
  cargandoHistorial = signal(false);
  mensajeError = signal('');

  form;

  constructor(
    private fb: FormBuilder,
    private animalService: AnimalService,
    private eventoService: EventoSanitarioService
  ) {
    this.form = this.fb.nonNullable.group({
      animalId: [null as number | null, Validators.required],
      tipo: ['VACUNA' as 'VACUNA' | 'CASTRACION' | 'DESPARASITACION', Validators.required],
      fecha: [new Date().toISOString().substring(0, 10), Validators.required],
      observaciones: [''],
    });
  }

  ngOnInit(): void {
    this.animalService.listarTodos().subscribe({
      next: (data) => {
        this.animales.set(data);
        this.cargandoAnimales.set(false);
      },
      error: () => {
        this.mensajeError.set('No se pudieron cargar los animales.');
        this.cargandoAnimales.set(false);
      }
    });
  }

  seleccionarAnimal(id: number): void {
    this.animalSeleccionadoId = id;
    this.cargandoHistorial.set(true);
    this.eventoService.obtenerHistorial(id).subscribe({
      next: (data) => {
        this.historial.set(data);
        this.cargandoHistorial.set(false);
      },
      error: () => {
        this.mensajeError.set('No se pudo cargar el historial de este animal.');
        this.cargandoHistorial.set(false);
      }
    });
  }

  agregarRegistro(): void {
    if (this.form.invalid) return;

    const valores = this.form.getRawValue();
    this.eventoService.registrarEvento({
      tipo: valores.tipo,
      fecha: valores.fecha,
      observaciones: valores.observaciones,
      animalId: valores.animalId!,
    }).subscribe({
      next: () => {
        if (this.animalSeleccionadoId === valores.animalId) {
          this.seleccionarAnimal(valores.animalId!);
        }
        this.form.patchValue({ observaciones: '' });
      },
      error: () => this.mensajeError.set('No se pudo registrar el evento sanitario.')
    });
  }

  eliminarEvento(id: number): void {
    const confirmado = confirm('¿Seguro que querés eliminar este registro sanitario?');
    if (!confirmado) return;

    this.eventoService.eliminarEvento(id).subscribe({
      next: () => {
        if (this.animalSeleccionadoId) {
          this.seleccionarAnimal(this.animalSeleccionadoId);
        }
      },
      error: () => this.mensajeError.set('No se pudo eliminar el registro.')
    });
  }
}
