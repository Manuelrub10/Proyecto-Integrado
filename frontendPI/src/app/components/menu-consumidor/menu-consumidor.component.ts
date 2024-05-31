import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-menu-consumidor',
  standalone: true,
  imports: [],
  templateUrl: './menu-consumidor.component.html',
  styleUrl: './menu-consumidor.component.css',
})
export class MenuConsumidorComponent {
  // Objeto para almacenar los datos del consumidor
  consumidor: any = {};

  // Constructor que inyecta los servicios Router y LoginService
  constructor(private router: Router, private loginService: LoginService) {}

  /**
   * Método que se ejecuta al inicializar el componente
   */
  ngOnInit() {
    // Intenta obtener el ID del usuario actual del localStorage
    const usuarioIdStr = localStorage.getItem('usuarioActualId');
    const usuarioId = usuarioIdStr ? Number(usuarioIdStr) : null;

    // Si el ID del usuario es válido
    if (usuarioId && !isNaN(usuarioId)) {
      // Llama al servicio de login para obtener los detalles del usuario
      this.loginService.getDetallesUsuario(usuarioId).subscribe({
        // Maneja la respuesta en caso de éxito
        next: (user) => {
          this.consumidor = user.consumidor; // Almacena los datos del consumidor
        },
        // Maneja la respuesta en caso de error
        error: (error) => {
          this.router.navigate(['/login']); // Navega a la página de login si hay un error
        },
      });
    } else {
      this.router.navigate(['/login']); // Navega a la página de login si el ID del usuario no es válido
    }
  }

  /**
   * Método para cerrar sesión
   */
  logout() {
    this.loginService.logout(); // Llama al servicio de login para cerrar sesión
    this.router.navigate(['/login']); // Navega a la página de login
  }

  /**
   * Método para navegar a la página de inicio del consumidor
   */
  irAHome() {
    this.router.navigate(['/home-consumidor']); // Navega a la página de inicio del consumidor
  }

  /**
   * Método para navegar al perfil del consumidor
   */
  irAMiPerfil() {
    if (this.consumidor && this.consumidor.id) {
      // Navega a la página de perfil del consumidor con el ID del consumidor
      this.router.navigate(['/consumidor-perfil', this.consumidor.id]);
    }
  }
}
