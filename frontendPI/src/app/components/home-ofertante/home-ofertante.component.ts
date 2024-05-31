import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { ActividadService } from '../../services/actividad.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OfertanteService } from '../../services/ofertante.service';
import { MenuOfertanteComponent } from '../menu-ofertante/menu-ofertante.component';
import { GestionReservasComponent } from '../gestion-reservas/gestion-reservas.component';

@Component({
  selector: 'app-home-ofertante',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    MenuOfertanteComponent,
    GestionReservasComponent,
  ],
  templateUrl: './home-ofertante.component.html',
  styleUrls: ['./home-ofertante.component.css'],
})
export class HomeOfertanteComponent implements OnInit {
  // Arreglo para almacenar las actividades del ofertante
  actividades: any[] = [];
  ofertante: any = {}; // Objeto para almacenar los datos del ofertante
  existActividades: Boolean = false; // Booleano para indicar si existen actividades
  page: number = 0; // Página actual para la paginación
  size: number = 1; // Tamaño de la página para la paginación
  totalPages: number = 0; // Número total de páginas
  mensaje: string = ''; // Mensaje para mostrar en la UI
  showGestionReserva = false; // Booleano para mostrar u ocultar la gestión de reservas
  idActividad: number = 0; // ID de la actividad para gestionar reservas

  // Constructor que inyecta los servicios necesarios
  constructor(
    private actividadService: ActividadService,
    private loginService: LoginService,
    private ofertanteService: OfertanteService,
    private router: Router
  ) {}

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
          this.ofertante = user.ofertante;
          this.cargarActividades(this.ofertante.id);
        },
        // Maneja la respuesta en caso de error
        error: (error) => {
          this.router.navigate(['/login']);
        },
      });
    } else {
      this.router.navigate(['/login']);
    }
  }

  /**
   * Método para mostrar u ocultar la gestión de reservas
   * @param actividadId ID de la actividad para gestionar reservas
   */
  toggleGestionReserva(actividadId: number) {
    if (this.showGestionReserva && this.idActividad === actividadId) {
      this.showGestionReserva = false;
      this.idActividad = -1;
    } else {
      this.showGestionReserva = true;
      this.idActividad = actividadId;
    }
  }

  /**
   * Método para cargar las actividades del ofertante
   * @param ofertanteId ID del ofertante
   */
  cargarActividades(ofertanteId: number) {
    this.actividadService
      .obtenerActividadesPorOfertante(ofertanteId, this.page, this.size)
      .subscribe({
        // Maneja la respuesta en caso de éxito
        next: (data) => {
          this.actividades = data.content;
          this.totalPages = data.totalPages;
          this.existActividades = this.actividades.length > 0;
        },
        // Maneja la respuesta en caso de error
        error: (error) => {
          this.actividades = [];
        },
      });
  }

  /**
   * Método para actualizar los datos del ofertante
   */
  actualizarOfertante(): void {
    const usuarioId = localStorage.getItem('usuarioActualId');
    if (usuarioId) {
      const id = Number(usuarioId);
      this.loginService.getDetallesUsuario(id).subscribe({
        next: (user) => {
          if (user && user.ofertante && user.ofertante.id) {
            const ofertanteId = user.ofertante.id;
            this.ofertanteService
              .editarOfertante(ofertanteId, this.ofertante)
              .subscribe({
                next: (response) => {
                  this.loginService.actualizarUsuarioActual(response);
                  this.mensaje = 'Se han guardado los cambios correctamente';
                  setTimeout(() => {
                    window.location.reload();
                    this.mensaje = '';
                  }, 3000);
                  this.router.navigate(['/home-ofertante']);
                },
                error: (error) => {
                  alert(
                    'Error al actualizar el ofertante. Por favor, inténtelo de nuevo más tarde.'
                  );
                },
              });
          } else {
            this.router.navigate(['/login']);
          }
        },
        error: (error) => {
          this.router.navigate(['/login']);
        },
      });
    } else {
      this.router.navigate(['/login']);
    }
  }

  /**
   * Método para eliminar al ofertante
   */
  eliminarOfertante() {
    if (confirm('¿Estás seguro de que deseas eliminar este ofertante?')) {
      const usuarioId = localStorage.getItem('usuarioActualId');
      if (usuarioId) {
        const id = Number(usuarioId);
        this.loginService.getDetallesUsuario(id).subscribe({
          next: (user) => {
            if (user && user.ofertante && user.ofertante.id) {
              const ofertanteId = user.ofertante.id;
              this.ofertanteService.eliminarOfertante(ofertanteId).subscribe({
                next: (response) => {
                  this.router.navigate(['/']);
                },
                error: (error) => {
                  alert(
                    'Este ofertante tiene actividades activas, borra las actividades para borrar al ofertante.'
                  );
                },
              });
            } else {
              this.router.navigate(['/login']);
            }
          },
          error: (error) => {
            this.router.navigate(['/login']);
          },
        });
      } else {
        this.router.navigate(['/login']);
      }
    }
  }

  /**
   * Método para cambiar de página en la lista de actividades
   * @param page Número de la nueva página
   */
  cambiarPagina(page: number) {
    const ofertanteId = this.ofertante.id;
    if (page >= 0 && page < this.totalPages) {
      this.page = page;
      this.cargarActividades(ofertanteId);
    }
  }

  /**
   * Método para navegar a la página de edición de una actividad
   * @param id ID de la actividad a editar
   */
  editarActividad(id: number) {
    this.router.navigate([`/editar-actividad/${id}`]);
  }

  /**
   * Método para eliminar una actividad
   * @param actividadId ID de la actividad a eliminar
   */
  eliminarActividad(actividadId: number) {
    if (confirm('¿Estás seguro de que deseas eliminar esta actividad?')) {
      this.actividadService.eliminarActividad(actividadId).subscribe({
        next: () => {
          this.actividades = this.actividades.filter(
            (a) => a.id !== actividadId
          );
          this.existActividades = this.actividades.length > 0;
          window.location.reload(); // Recarga la página
        },
        error: (error) => {
          alert(
            'No se puede eliminar la actividad porque tiene reservas activas.'
          );
        },
      });
    }
  }

  /**
   * Método para cerrar sesión
   */
  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  /**
   * Método para navegar a la página de creación de una nueva actividad
   */
  irACrearActividad() {
    this.router.navigate(['/crear-actividad']);
  }

  /**
   * Método para navegar a la página de gestión de reservas de una actividad
   * @param actividadId ID de la actividad para gestionar reservas
   */
  irAGestionarReserva(actividadId: number) {
    this.router.navigate(['/actividad', actividadId, 'gestion-reservas']);
  }

  /**
   * Método para navegar al perfil del ofertante
   */
  irAMiPerfil() {
    if (this.ofertante && this.ofertante.id) {
      this.router.navigate([`/editar-ofertante/${this.ofertante.id}`]);
    }
  }
}
