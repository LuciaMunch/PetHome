import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private readonly apiUrl = 'http://localhost:8080/api/catalogo';

  constructor(private http: HttpClient) {}

  // Trae los animales del catálogo, con filtros opcionales
  listar(filtros: {
    especie?: string;
    sexo?: string;
    tamanio?: string;
    edadMax?: number;
  }): Observable<any> {
    let params: any = {};
    if (filtros.especie)  params.especie = filtros.especie;
    if (filtros.sexo)     params.sexo = filtros.sexo;
    if (filtros.tamanio)  params.tamanio = filtros.tamanio;
    if (filtros.edadMax != null) params.edadMax = filtros.edadMax;

    return this.http.get<any>(this.apiUrl, { params });
  }
}
