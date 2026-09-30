import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UsuarioResponse {
  id: number;
  nombreUsuario: string;
  email: string;
  rol: string;
}

export interface PerfilResponse {
  id: number;
  nombreUsuario: string;
  email: string;
  rol: string;
  nombreCompleto: string | null;
  direccion: string | null;
  ciudad: string | null;
  provincia: string | null;
  telefono: string | null;
  edad: number | null;
}
export interface PerfilRequest {
  direccion: string | null;
  ciudad: string | null;
  provincia: string | null;
  telefono: string | null;
  edad: number | null;
}
@Injectable({ providedIn: 'root' })
export class UsuariosService {
  private readonly apiUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  listarAdoptantes(): Observable<UsuarioResponse[]> {
    return this.http.get<UsuarioResponse[]>(this.apiUrl);
  }

  obtenerMiPerfil(): Observable<PerfilResponse> {
    return this.http.get<PerfilResponse>(`${this.apiUrl}/me`);
  }

  actualizarMiPerfil(data: PerfilRequest): Observable<PerfilResponse> {
    return this.http.put<PerfilResponse>(`${this.apiUrl}/me`, data);
  }
}
