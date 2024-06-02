import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReservaService {
  // URL base de la API de reservas
  private apiUrl = 'http://localhost:8081/api/reservas';

  // Constructor que inyecta el servicio HttpClient
  constructor(private http: HttpClient) {}

  /**
   * Método para crear una reserva
   * @param actividadId ID de la actividad
   * @param consumidorId ID del consumidor
   * @returns Observable con la respuesta de la solicitud POST
   */
  createReserva(actividadId: number, consumidorId: number): Observable<any> {
    // Realiza una solicitud POST a la API para crear una reserva
    return this.http.post<any>(
      `${this.apiUrl}?actividadId=${actividadId}&consumidorId=${consumidorId}`, // Construye la URL con los parámetros de consulta
      {}
    );
  }

  /**
   * Método para obtener reservas de un consumidor con paginación
   * @param consumidorId ID del consumidor
   * @param page Número de página
   * @param size Tamaño de la página
   * @returns Observable con la lista de reservas del consumidor
   */
  obtenerReservasPorConsumidor(
    consumidorId: number,
    page: number,
    size: number
  ): Observable<any> {
    // Realiza una solicitud GET a la API para obtener reservas paginadas por consumidor
    return this.http.get<any>(
      `${this.apiUrl}/consumidor/${consumidorId}?page=${page}&size=${size}` // Construye la URL con los parámetros de consulta
    );
  }

  /**
   * Método para verificar si existe una reserva
   * @param actividadId ID de la actividad
   * @param consumidorId ID del consumidor
   * @returns Observable con un booleano indicando si la reserva existe
   */
  existeReserva(
    actividadId: number,
    consumidorId: number
  ): Observable<boolean> {
    // Realiza una solicitud GET a la API para verificar si existe una reserva
    return this.http.get<boolean>(
      `${this.apiUrl}/existe?actividadId=${actividadId}&consumidorId=${consumidorId}` // Construye la URL con los parámetros de consulta
    );
  }

  /**
   * Método para obtener reservas por actividad
   * @param actividadId ID de la actividad
   * @returns Observable con la lista de reservas de la actividad
   */
  getReservasByActividad(actividadId: number): Observable<any[]> {
    // Realiza una solicitud GET a la API para obtener reservas por actividad
    return this.http.get<any[]>(`${this.apiUrl}/actividad/${actividadId}`); // Construye la URL con el ID de la actividad
  }

  /**
   * Método para cancelar una reserva
   * @param id ID de la reserva a cancelar
   * @returns Observable void, sin contenido en la respuesta
   */
  cancelarReserva(id: number): Observable<void> {
    // Realiza una solicitud DELETE a la API para cancelar una reserva
    return this.http.delete<void>(`${this.apiUrl}/${id}`); // Construye la URL con el ID de la reserva
  }
}
