import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ConsumidorService {
  // URL base de la API de consumidores
  private apiUrl = 'http://localhost:8081/api/consumidores';

  // Constructor que inyecta el servicio HttpClient
  constructor(private http: HttpClient) {}

  /**
   * Método para obtener un consumidor por su ID
   * @param id ID del consumidor
   * @returns Observable con la respuesta de la solicitud GET
   */
  obtenerConsumidorPorId(id: number): Observable<any> {
    // Realiza una solicitud GET a la API para obtener un consumidor por su ID
    return this.http.get<any>(`${this.apiUrl}/${id}`); // Construye la URL con el ID del consumidor
  }

  /**
   * Método para editar un consumidor
   * @param id ID del consumidor a editar
   * @param consumidor Datos del consumidor a actualizar
   * @returns Observable con la respuesta de la solicitud PUT
   */
  editarConsumidor(id: number, consumidor: any): Observable<any> {
    // Realiza una solicitud PUT a la API para actualizar un consumidor
    return this.http.put<any>(`${this.apiUrl}/editar/${id}`, consumidor); // Construye la URL con el ID del consumidor y envía los datos actualizados
  }

  /**
   * Método para eliminar un consumidor
   * @param id ID del consumidor a eliminar
   * @returns Observable con la respuesta de la solicitud DELETE
   */
  eliminarConsumidor(id: number): Observable<any> {
    // Realiza una solicitud DELETE a la API para eliminar un consumidor
    return this.http.delete<any>(`${this.apiUrl}/eliminar/${id}`); // Construye la URL con el ID del consumidor a eliminar
  }
}
