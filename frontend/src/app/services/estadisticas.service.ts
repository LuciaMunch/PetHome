import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface EstadisticasResponse {
  totalAnimales: number;
  adoptados: number;
  animalesPorEstado: Record<string, number>;
  animalesPorEspecie: Record<string, number>;
  animalesPorTamanio: Record<string, number>;
  solicitudesPendientes: number;
  solicitudesAprobadas: number;
  solicitudesRechazadas: number;
  totalAdoptantes: number;
  adopcionesPorMes: Record<string, number>;
}

@Injectable({ providedIn: 'root' })
export class EstadisticasService {
  private readonly apiUrl = 'http://localhost:8080/api/estadisticas';

  constructor(private http: HttpClient) {}

  obtenerResumen(): Observable<EstadisticasResponse> {
    return this.http.get<EstadisticasResponse>(this.apiUrl);
  }
}
