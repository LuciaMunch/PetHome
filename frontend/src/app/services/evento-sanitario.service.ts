import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface EventoSanitarioRequest {
  tipo: 'VACUNA' | 'CASTRACION' | 'DESPARASITACION';
  fecha: string;
  observaciones: string;
  animalId: number;
}

export interface EventoSanitarioResponse {
  id: number;
  tipo: 'VACUNA' | 'CASTRACION' | 'DESPARASITACION';
  fecha: string;
  observaciones: string;
  animalId: number;
}

@Injectable({ providedIn: 'root' })
export class EventoSanitarioService {
  private readonly apiUrl = 'http://localhost:8080/api/eventos-sanitarios';

  constructor(private http: HttpClient) {}

  registrarEvento(data: EventoSanitarioRequest): Observable<EventoSanitarioResponse> {
    return this.http.post<EventoSanitarioResponse>(this.apiUrl, data);
  }

  obtenerHistorial(animalId: number): Observable<EventoSanitarioResponse[]> {
    return this.http.get<EventoSanitarioResponse[]>(`${this.apiUrl}/animal/${animalId}`);
  }
}
