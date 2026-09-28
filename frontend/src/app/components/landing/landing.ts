import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CatalogoService } from '../../services/catalogo.service';

interface AnimalDestacado {
  id: number;
  nombre: string;
  fotoUrl?: string;
  estado?: string;
  edad?: number;
  sexo?: string;
  tamanio?: string;
}

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing implements OnInit, OnDestroy {

  animales: AnimalDestacado[] = [];
  indiceActual = 0;
  cargandoAnimal = true;
  aliasCopiado = false;

  private intervalId: any;

  constructor(
    private catalogoService: CatalogoService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.catalogoService.listar({}).subscribe({
      next: (data) => {
        const animales: AnimalDestacado[] = Array.isArray(data) ? data : (data?.content ?? []);
        this.animales = animales.filter(a => a.fotoUrl);
        this.cargandoAnimal = false;
        this.cdr.detectChanges();

        if (this.animales.length > 1) {
          this.intervalId = setInterval(() => {
            this.indiceActual = (this.indiceActual + 1) % this.animales.length;
            this.cdr.detectChanges();
          }, 6000);
        }
      },
      error: () => {
        this.cargandoAnimal = false;
        this.cdr.detectChanges();
      }
    });
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  get animalActual(): AnimalDestacado | null {
    return this.animales[this.indiceActual] ?? null;
  }

  irACatalogo(): void {
    this.router.navigate(['/catalogo']);
  }

  verPerfil(): void {
    if (this.animalActual) {
      this.router.navigate(['/animal', this.animalActual.id]);
    }
  }

  // Hace scroll animado hasta el elemento con el id indicado, tardando "duracionMs" milisegundos
  private scrollSuaveA(id: string, duracionMs = 1200): void {
    const elemento = document.getElementById(id);
    if (!elemento) return;

    const inicio = window.scrollY;
    const destino = elemento.getBoundingClientRect().top + window.scrollY;
    const distancia = destino - inicio;
    const tiempoInicio = performance.now();

    const paso = (ahora: number) => {
      const transcurrido = ahora - tiempoInicio;
      const progreso = Math.min(transcurrido / duracionMs, 1);
      // easing suave: arranca lento, acelera en el medio, frena al llegar
      const facilitado = progreso < 0.5
        ? 2 * progreso * progreso
        : 1 - Math.pow(-2 * progreso + 2, 2) / 2;

      window.scrollTo(0, inicio + distancia * facilitado);

      if (progreso < 1) {
        requestAnimationFrame(paso);
      }
    };

    requestAnimationFrame(paso);
  }

  scrollAPasos(): void {
    this.scrollSuaveA('pasos');
  }

  scrollAHistoria(): void {
    this.scrollSuaveA('historia');
  }

  copiarAlias(): void {
    navigator.clipboard.writeText('PETHOME.MP').then(() => {
      this.aliasCopiado = true;
      this.cdr.detectChanges();
      setTimeout(() => {
        this.aliasCopiado = false;
        this.cdr.detectChanges();
      }, 2000);
    });
  }
}
