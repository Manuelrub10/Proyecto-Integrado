import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ActividadService {
  // URL base de la API de actividades
  private apiUrl = 'http://localhost:8081/api/actividades';

  // Constructor que inyecta el servicio HttpClient
  constructor(private http: HttpClient) {}

  /**
   * Método para obtener todas las actividades con paginación
   * @param page Número de página
   * @param size Tamaño de la página
   * @returns Observable con la respuesta de la solicitud GET
   */
  obtenerTodasLasActividades(page: number, size: number): Observable<any> {
    // Realiza una solicitud GET a la API para obtener todas las actividades con paginación
    return this.http.get<any>(`${this.apiUrl}/todas?page=${page}&size=${size}`);
  }

  /**
   * Método para obtener una actividad por su ID
   * @param id ID de la actividad
   * @returns Observable con la respuesta de la solicitud GET
   */
  obtenerActividadPorId(id: number): Observable<any> {
    // Realiza una solicitud GET a la API para obtener una actividad por su ID
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  /**
   * Método para obtener actividades por ID de ofertante con paginación
   * @param ofertanteId ID del ofertante
   * @param page Número de página
   * @param size Tamaño de la página
   * @returns Observable con la respuesta de la solicitud GET
   */
  obtenerActividadesPorOfertante(
    ofertanteId: number,
    page: number,
    size: number
  ): Observable<any> {
    // Realiza una solicitud GET a la API para obtener actividades por ID de ofertante con paginación
    return this.http.get<any>(
      `${this.apiUrl}/ofertante/${ofertanteId}?page=${page}&size=${size}`
    );
  }

  /**
   * Método para crear una nueva actividad
   * @param actividad Datos de la actividad a crear
   * @returns Observable con la respuesta de la solicitud POST
   */
  crearActividad(actividad: any): Observable<any> {
    // Realiza una solicitud POST a la API para crear una nueva actividad
    return this.http.post<any>(`${this.apiUrl}/crear`, actividad);
  }

  /**
   * Método para editar una actividad existente
   * @param id ID de la actividad a editar
   * @param actividad Datos de la actividad a actualizar
   * @returns Observable con la respuesta de la solicitud PUT
   */
  editarActividad(id: number, actividad: any): Observable<any> {
    // Realiza una solicitud PUT a la API para actualizar una actividad existente
    return this.http.put<any>(`${this.apiUrl}/editar/${id}`, actividad);
  }

  /**
   * Método para eliminar una actividad
   * @param id ID de la actividad a eliminar
   * @returns Observable con la respuesta de la solicitud DELETE
   */
  eliminarActividad(id: number): Observable<void> {
    // Realiza una solicitud DELETE a la API para eliminar una actividad
    return this.http.delete<void>(`${this.apiUrl}/eliminar/${id}`);
  }

  /**
   * Método para verificar si una actividad tiene reservas activas
   * @param actividadId ID de la actividad
   * @returns Observable con un booleano indicando si la actividad tiene reservas activas
   */
  verificarReservasActivas(actividadId: number): Observable<boolean> {
    // Realiza una solicitud GET a la API para verificar si una actividad tiene reservas activas
    return this.http.get<boolean>(
      `${this.apiUrl}/${actividadId}/tiene-reservas-activas`
    );
  }
}
