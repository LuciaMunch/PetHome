import { Component, OnInit } from '@angular/core';
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

  animales: AnimalOpcion[] = [];
  animalSeleccionadoId: number | null = null;
  historial: EventoSanitarioResponse[] = [];

  cargandoAnimales = true;
  cargandoHistorial = false;
  mensajeError = '';

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
        this.animales = data;
        this.cargandoAnimales = false;
      },
      error: () => {
        this.mensajeError = 'No se pudieron cargar los animales.';
        this.cargandoAnimales = false;
      }
    });
  }

  seleccionarAnimal(id: number): void {
    this.animalSeleccionadoId = id;
    this.cargandoHistorial = true;
    this.eventoService.obtenerHistorial(id).subscribe({
      next: (data) => {
        this.historial = data;
        this.cargandoHistorial = false;
      },
      error: () => {
        this.mensajeError = 'No se pudo cargar el historial de este animal.';
        this.cargandoHistorial = false;
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
      error: () => this.mensajeError = 'No se pudo registrar el evento sanitario.'
    });
  }
}
