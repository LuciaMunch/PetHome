import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AnimalService {
  private readonly apiUrl = 'http://localhost:8080/api/animales';

  constructor(private http: HttpClient) {}

  // Traer un animal por su id
  obtenerPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Traer las fotos de un animal (galería)
  obtenerFotos(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}/fotos`);
  }

  // Crear un animal nuevo
  crear(animal: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, animal);
  }

  // Editar un animal existente
  actualizar(id: number, animal: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, animal);
  }

  // Borrar un animal
  eliminar(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }

  // Subir una foto (archivo) a un animal
  subirFoto(id: number, archivo: File): Observable<any> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<any>(`${this.apiUrl}/${id}/fotos`, formData);
  }

  // Traer todos los animales (para el ABM admin)
  listarTodos(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }
}
