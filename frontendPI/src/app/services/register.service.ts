import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  // URL base de la API de registro de usuarios
  private apiUrl = 'http://localhost:8081/api/usuarios/register';

  // Constructor que inyecta el servicio HttpClient
  constructor(private http: HttpClient) {}

  /**
   * Método para registrar un nuevo usuario
   * @param registerData Datos de registro del usuario
   * @returns Observable con la respuesta de la solicitud POST
   */
  register(registerData: any): Observable<any> {
    // Realiza una solicitud POST a la API para registrar un nuevo usuario
    return this.http
      .post(this.apiUrl, registerData, { observe: 'response' }) // Envía los datos de registro y observa la respuesta completa
      .pipe(
        catchError(this.handleError) // Maneja los errores de la solicitud
      );
  }

  /**
   * Manejo de errores para solicitudes HTTP
   * @param error Objeto de error
   * @returns Observable que lanza un error
   */
  private handleError(error: any) {
    // Crea y lanza un nuevo error con un mensaje personalizado
    return throwError(
      () => new Error('Algo ha pasado. Por favor intentalo de nuevo mas tarde.')
    );
  }
}
