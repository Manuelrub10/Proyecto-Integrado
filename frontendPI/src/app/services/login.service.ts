import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, catchError, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  // URL base de la API de usuarios
  private apiUrl = 'http://localhost:8081/api/usuarios';

  // BehaviorSubject para mantener el estado del usuario actual
  private usuarioActualSubject: BehaviorSubject<any>;
  // Observable público para que otros componentes puedan suscribirse al estado del usuario actual
  public usuarioActual: Observable<any>;

  // Constructor que inyecta los servicios HttpClient y Router
  constructor(private http: HttpClient, private router: Router) {
    // Intenta obtener el ID del usuario actual del localStorage
    const usuarioId = localStorage.getItem('usuarioActualId');
    // Inicializa el BehaviorSubject con el usuario actual o null
    this.usuarioActualSubject = new BehaviorSubject<any>(
      usuarioId ? { id: Number(usuarioId) } : null
    );
    // Asigna el BehaviorSubject como un Observable para que pueda ser observado
    this.usuarioActual = this.usuarioActualSubject.asObservable();
  }

  /**
   * Getter para obtener el valor actual del usuario
   * @returns Valor actual del usuario
   */
  public get valorUsuarioActual() {
    return this.usuarioActualSubject.value;
  }

  /**
   * Método para iniciar sesión
   * @param email Correo electrónico del usuario
   * @param password Contraseña del usuario
   * @param role Rol del usuario (consumidor u ofertante)
   * @returns Observable con la respuesta de la solicitud POST
   */
  login(email: string, password: string, role: string): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}/login`, { email, password, role }) // Realiza una solicitud POST con las credenciales
      .pipe(
        tap((response) => {
          // Si la respuesta contiene un token y un usuario
          if (response && response.token && response.usuario) {
            // Almacena el ID del usuario en el localStorage
            localStorage.setItem(
              'usuarioActualId',
              JSON.stringify(response.usuario.id)
            );
            // Actualiza el BehaviorSubject con el usuario actual
            this.usuarioActualSubject.next({
              id: response.usuario.id,
              ...response.usuario,
            });

            // Navega a la página de inicio correspondiente según el rol del usuario
            if (role === 'consumidor') {
              this.router.navigate(['/home-consumidor']);
            } else if (role === 'ofertante') {
              this.router.navigate(['/home-ofertante']);
            }
          }
        }),
        catchError((error) => {
          // Maneja los errores y lanza un nuevo error con un mensaje personalizado
          return throwError(() => new Error('Error en el proceso de login'));
        })
      );
  }

  /**
   * Método para cerrar sesión
   */
  logout() {
    // Elimina el ID del usuario del localStorage
    localStorage.removeItem('usuarioActualId');
    // Actualiza el BehaviorSubject a null
    this.usuarioActualSubject.next(null);
    // Navega a la página de login
    this.router.navigate(['/login']);
  }

  /**
   * Método para obtener los detalles de un usuario por su ID
   * @param id ID del usuario
   * @returns Observable con la respuesta de la solicitud GET
   */
  getDetallesUsuario(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      tap((usuario) => {
        // Obtiene el usuario actual
        const usuarioActual = this.valorUsuarioActual;
        if (usuarioActual) {
          // Actualiza el usuario actual con los detalles obtenidos
          usuarioActual.usuario = usuario;
          this.usuarioActualSubject.next(usuarioActual);
        }
      })
    );
  }

  /**
   * Método para actualizar el usuario actual
   * @param updatedUser Datos del usuario actualizados
   */
  actualizarUsuarioActual(updatedUser: any) {
    // Obtiene el usuario actual
    const currentUser = this.valorUsuarioActual;
    // Actualiza los datos del usuario actual
    currentUser.usuario = updatedUser;
    // Actualiza el ID del usuario en el localStorage
    localStorage.setItem('usuarioActualId', currentUser.id.toString());
    // Actualiza el BehaviorSubject con el usuario actualizado
    this.usuarioActualSubject.next(currentUser);
  }
}
