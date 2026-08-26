import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { SolicitudAdopcionService } from '../../services/solicitud-adopcion.service';

@Component({
  selector: 'app-modal-formulario-adopcion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './modal-formulario-adopcion.html',
  styleUrl: './modal-formulario-adopcion.css',
})
export class ModalFormularioAdopcion {

  @Input() animalId!: number;
  @Output() cerrar = new EventEmitter<void>();

  enviando = false;
  mensajeError = '';

  form;

  constructor(
    private fb: FormBuilder,
    private solicitudService: SolicitudAdopcionService
  ) {
    this.form = this.fb.nonNullable.group({
      tipoVivienda: ['CASA' as 'CASA' | 'DEPARTAMENTO', Validators.required],
      tienePatio: [true, Validators.required],
      integrantesHogar: [1, [Validators.required, Validators.min(1)]],
      otrasMascotas: [false, Validators.required],
      experienciaPrevia: [false, Validators.required],
      motivo: [''],
    });
  }

  enviar(): void {
    if (this.form.invalid) return;

    this.enviando = true;
    this.mensajeError = '';

    const payload = { ...this.form.getRawValue(), animalId: this.animalId };

    this.solicitudService.enviarSolicitud(payload).subscribe({
      next: () => {
        this.enviando = false;
        this.cerrar.emit();
      },
      error: (err) => {
        this.enviando = false;
        if (err.status === 409) {
          this.mensajeError = 'Ya tenés una solicitud pendiente para este animal.';
        } else if (err.status === 400) {
          this.mensajeError = 'Este animal ya no está disponible para adopción.';
        } else {
          this.mensajeError = 'Ocurrió un error al enviar la solicitud. Intentá de nuevo.';
        }
      }
    });
  }

  cancelar(): void {
    this.cerrar.emit();
  }
}
