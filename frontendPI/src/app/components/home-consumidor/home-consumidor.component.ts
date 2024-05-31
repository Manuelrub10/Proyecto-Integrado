import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActividadService } from '../../services/actividad.service';
import { Router } from '@angular/router';
import { ReservaService } from '../../services/reserva.service';
import { ConsumidorService } from '../../services/consumidor.service';
import { MenuConsumidorComponent } from '../menu-consumidor/menu-consumidor.component';

@Component({
  selector: 'app-home-consumidor',
  standalone: true,
  imports: [CommonModule, FormsModule, MenuConsumidorComponent],
  templateUrl: './home-consumidor.component.html',
  styleUrl: './home-consumidor.component.css',
})
export class HomeConsumidorComponent implements OnInit {
  // Arreglos para almacenar actividades y reservas
  actividades: any[] = [];
  reservas: any[] = [];
  consumidor: any = {}; // Objeto para almacenar los datos del consumidor

  // Propiedades para manejar la paginación de actividades
  page: number = 0;
  size: number = 6;
  totalPages: number = 0;

  // Constructor que inyecta los servicios necesarios
  constructor(
    private loginService: LoginService,
    private router: Router,
    private actividadService: ActividadService,
    private reservaService: ReservaService
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
          this.consumidor = user.consumidor; // Almacena los datos del consumidor
          this.cargarActividades(); // Carga las actividades disponibles
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
   * Método para cargar las actividades disponibles
   */
  cargarActividades() {
    // Llama al servicio de actividades para obtener las actividades con paginación
    this.actividadService
      .obtenerTodasLasActividades(this.page, this.size)
      .subscribe({
        // Maneja la respuesta en caso de éxito
        next: (data) => {
          this.actividades = data.content; // Almacena las actividades en la propiedad actividades
          this.totalPages = data.totalPages; // Almacena el número total de páginas
        },
        // Maneja la respuesta en caso de error
        error: (error) => {
          // Manejo de errores (actualmente vacío)
        },
      });
  }

  /**
   * Método para realizar una reserva en una actividad
   * @param actividadId ID de la actividad a reservar
   */
  reservarActividad(actividadId: number) {
    const consumidorId = this.consumidor.id;
    const confirmacion = confirm(
      '¿Estás seguro de que deseas participar en esta actividad?'
    );

    if (confirmacion) {
      // Verifica si ya existe una reserva para esta actividad
      this.reservaService.existeReserva(actividadId, consumidorId).subscribe({
        next: (existe) => {
          if (existe) {
            alert('Ya tienes una reserva para esta actividad');
          } else {
            // Crea una nueva reserva si no existe
            this.reservaService
              .createReserva(actividadId, consumidorId)
              .subscribe({
                next: (data) => {
                  alert('Reserva realizada con éxito');
                },
                error: (error) => {
                  if (error.status === 400) {
                    alert(error.error.message);
                  } else {
                    alert(
                      'Ocurrió un error al realizar la reserva. Por favor, inténtalo de nuevo.'
                    );
                  }
                },
              });
          }
        },
        error: (error) => {
          alert(
            'Ocurrió un error al verificar la existencia de la reserva. Por favor, inténtalo de nuevo.'
          );
        },
      });
    }
  }

  /**
   * Método para cambiar de página en la lista de actividades
   * @param page Número de la nueva página
   */
  cambiarPagina(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.page = page; // Establece la nueva página
      this.cargarActividades(); // Carga las actividades de la nueva página
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
   * Método para navegar al perfil del consumidor
   */
  irAMiPerfil() {
    if (this.consumidor && this.consumidor.id) {
      this.router.navigate(['/editar-consumidor', this.consumidor.id]); // Navega a la página de perfil del consumidor
    }
  }
}
