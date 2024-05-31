import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-menu-ofertante',
  standalone: true,
  imports: [],
  templateUrl: './menu-ofertante.component.html',
  styleUrl: './menu-ofertante.component.css',
})
export class MenuOfertanteComponent {
  // Objeto para almacenar los datos del ofertante
  ofertante: any = {};

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
          this.ofertante = user.ofertante; // Almacena los datos del ofertante
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
   * Método para navegar a la página de inicio del ofertante
   */
  irAHome() {
    this.router.navigate(['/home-ofertante']); // Navega a la página de inicio del ofertante
  }

  /**
   * Método para navegar al perfil del ofertante
   */
  irAMiPerfil() {
    if (this.ofertante && this.ofertante.id) {
      // Navega a la página de perfil del ofertante con el ID del ofertante
      this.router.navigate(['/ofertante-perfil', this.ofertante.id]);
    }
  }
}
