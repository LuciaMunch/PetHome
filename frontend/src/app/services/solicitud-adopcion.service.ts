import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
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

  obtenerPendientes(page = 0, size = 20): Observable<PageResponse<SolicitudAdopcionResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<SolicitudAdopcionResponse>>(`${this.apiUrl}/pendientes`, { params });
  }

  aprobar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/aprobar`, {});
  }

  rechazar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/rechazar`, {});
  }
}

