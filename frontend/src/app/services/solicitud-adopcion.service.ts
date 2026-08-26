import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SolicitudAdopcionRequest {
  tipoVivienda: 'CASA' | 'DEPARTAMENTO';
  tienePatio: boolean;
  integrantesHogar: number;
  otrasMascotas: boolean;
  experienciaPrevia: boolean;
  motivo: string;
  animalId: number;
}

export interface SolicitudAdopcionResponse {
  id: number;
  fecha: string;
  estado: 'PENDIENTE' | 'APROBADA' | 'RECHAZADA';
  tipoVivienda: 'CASA' | 'DEPARTAMENTO';
  tienePatio: boolean;
  integrantesHogar: number;
  otrasMascotas: boolean;
  experienciaPrevia: boolean;
  motivo: string;
  animalId: number;
  usuarioId: number;
}

@Injectable({ providedIn: 'root' })
export class SolicitudAdopcionService {
  private readonly apiUrl = 'http://localhost:8080/api/solicitudes-adopcion';

  constructor(private http: HttpClient) {}

  enviarSolicitud(data: SolicitudAdopcionRequest): Observable<any> {
    return this.http.post<any>(this.apiUrl, data);
  }

  misSolicitudes(): Observable<SolicitudAdopcionResponse[]> {
    return this.http.get<SolicitudAdopcionResponse[]>(`${this.apiUrl}/mis-solicitudes`);
  }
}
