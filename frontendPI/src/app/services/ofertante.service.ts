import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class OfertanteService {
  // URL base de la API de ofertantes
  private apiUrl = 'http://localhost:8080/api/ofertantes';

  // Constructor que inyecta el servicio HttpClient
  constructor(private http: HttpClient) {}

  /**
   * Método para obtener un ofertante por su ID
   * @param id ID del ofertante
   * @returns Observable con la respuesta de la solicitud GET
   */
  obtenerOfertantePorId(id: number): Observable<any> {
    // Realiza una solicitud GET a la API para obtener un ofertante por su ID
    return this.http.get<any>(`${this.apiUrl}/${id}`); // Construye la URL con el ID del ofertante
  }

  /**
   * Método para editar un ofertante
   * @param id ID del ofertante a editar
   * @param ofertante Datos del ofertante a actualizar
   * @returns Observable con la respuesta de la solicitud PUT
   */
  editarOfertante(id: number, ofertante: any): Observable<any> {
    // Realiza una solicitud PUT a la API para actualizar un ofertante
    return this.http.put<any>(`${this.apiUrl}/editar/${id}`, ofertante); // Construye la URL con el ID del ofertante y envía los datos actualizados
  }

  /**
   * Método para eliminar un ofertante
   * @param id ID del ofertante a eliminar
   * @returns Observable con la respuesta de la solicitud DELETE
   */
  eliminarOfertante(id: number): Observable<any> {
    // Realiza una solicitud DELETE a la API para eliminar un ofertante
    return this.http.delete<any>(`${this.apiUrl}/eliminar/${id}`); // Construye la URL con el ID del ofertante a eliminar
  }
}
