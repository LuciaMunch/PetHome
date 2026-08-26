import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

interface RegistroSanitario {
  id: number;
  animalNombre: string;
  tipoRegistro: string;
  descripcion: string;
  fecha: string;
}

@Component({
  selector: 'app-admin-sanitario',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-sanitario.html',
  styleUrl: './admin-sanitario.css',
})
export class AdminSanitario implements OnInit {

  // MOCK del Historial Sanitario (HU-14)
  registros: RegistroSanitario[] = [
    { id: 1, animalNombre: 'Luna', tipoRegistro: 'VACUNA', descripcion: 'Quíntuple canina aplicada.', fecha: '2026-05-01' },
    { id: 2, animalNombre: 'Michi', tipoRegistro: 'CASTRACION', descripcion: 'Cirugía realizada sin complicaciones.', fecha: '2026-04-20' },
  ];

  form;

  constructor(private fb: FormBuilder) {
    // Formulario para Registrar Evento Sanitario (HU-13)
    this.form = this.fb.nonNullable.group({
      animalNombre: ['', Validators.required],
      tipoRegistro: ['VACUNA', Validators.required],
      descripcion: ['', Validators.required],
      fecha: [new Date().toISOString().substring(0, 10), Validators.required]
    });
  }

  ngOnInit(): void {}

  agregarRegistro(): void {
    if (this.form.invalid) return;

    const nuevoRegistro: RegistroSanitario = {
      id: Date.now(),
      ...this.form.getRawValue()
    };

    this.registros.unshift(nuevoRegistro);
    this.form.reset({
      tipoRegistro: 'VACUNA',
      fecha: new Date().toISOString().substring(0, 10)
    });
  }
}
