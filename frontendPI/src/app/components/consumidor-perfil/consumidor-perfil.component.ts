import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MenuConsumidorComponent } from '../menu-consumidor/menu-consumidor.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ActividadService } from '../../services/actividad.service';
import { ConsumidorService } from '../../services/consumidor.service';
import { LoginService } from '../../services/login.service';
import { ReservaService } from '../../services/reserva.service';

@Component({
  selector: 'app-consumidor-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule, MenuConsumidorComponent],
  templateUrl: './consumidor-perfil.component.html',
  styleUrl: './consumidor-perfil.component.css',
})
export class ConsumidorPerfilComponent {
  // Objeto para almacenar los datos del consumidor
  consumidor: any = {
    nombre: '',
    apellidos: '',
    direccion: '',
    telefono: '',
    usuario: {},
  };

  // Arreglos para almacenar actividades y reservas
  actividades: any[] = [];
  reservas: any[] = [];

  // Propiedades para manejar la paginación de reservas
  page: number = 0;
  size: number = 2;
  totalPages: number = 0;

  // Constructor que inyecta los servicios necesarios
  constructor(
    private loginService: LoginService,
    private route: ActivatedRoute,
    private router: Router,
    private actividadService: ActividadService,
    private consumidorService: ConsumidorService,
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
          this.cargarReservas(); // Carga las reservas del consumidor
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
                  window.location.reload(); // Recarga la página después de crear la reserva
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
   * Método para cargar las reservas del consumidor
   */
  cargarReservas() {
    // Llama al servicio de reservas para obtener las reservas del consumidor
    this.reservaService
      .obtenerReservasPorConsumidor(this.consumidor.id, this.page, this.size)
      .subscribe({
        next: (data) => {
          this.reservas = data.content; // Almacena las reservas en la propiedad reservas
          this.totalPages = data.totalPages; // Almacena el número total de páginas
        },
        error: (error) => {
          // Manejo de errores (actualmente vacío)
        },
      });
  }

  /**
   * Método para cancelar una reserva
   * @param id ID de la reserva a cancelar
   */
  cancelarReserva(id: number) {
    if (confirm('¿Estás seguro de que deseas cancelar esta reserva?')) {
      // Llama al servicio de reservas para cancelar la reserva
      this.reservaService.cancelarReserva(id).subscribe({
        next: () => {
          this.cargarReservas(); // Recarga las reservas después de cancelar
        },
        error: (error) => {
          // Manejo de errores (actualmente vacío)
        },
      });
    }
  }

  /**
   * Método para cambiar de página en la lista de reservas
   * @param page Número de la nueva página
   */
  cambiarPagina(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.page = page; // Establece la nueva página
      this.cargarReservas(); // Carga las reservas de la nueva página
    }
  }

  /**
   * Método para actualizar los datos del consumidor
   */
  actualizarConsumidor() {
    const id = this.route.snapshot.paramMap.get('id');
    this.consumidorService
      .editarConsumidor(Number(id), this.consumidor)
      .subscribe({
        next: (response) => {
          const usuarioActual = this.loginService.valorUsuarioActual;
          if (usuarioActual && usuarioActual.id) {
            // Actualiza los detalles del usuario actual
            this.loginService
              .getDetallesUsuario(usuarioActual.id)
              .subscribe(() => {
                this.router.navigate(['/home-consumidor']); // Navega a la página de inicio del consumidor
              });
          }
        },
        error: (error) => {
          // Manejo de errores (actualmente vacío)
        },
      });
  }

  /**
   * Método para eliminar al consumidor
   */
  eliminarConsumidor() {
    if (confirm('¿Estás seguro de que deseas eliminar este consumidor?')) {
      const id = this.route.snapshot.paramMap.get('id');
      this.consumidorService.eliminarConsumidor(Number(id)).subscribe({
        next: (response) => {
          this.router.navigate(['/']); // Navega a la página principal después de eliminar
        },
        error: (error) => {
          alert(
            'No se ha podido eliminar la cuenta porque tienes reservas activas'
          );
        },
      });
    }
  }
}
