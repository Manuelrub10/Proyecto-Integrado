import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReservaService } from '../../services/reserva.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-gestion-reservas',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './gestion-reservas.component.html',
  styleUrl: './gestion-reservas.component.css',
})
export class GestionReservasComponent {
  // Propiedad para almacenar la lista de reservas
  reservas: any[] = [];

  // Propiedad de entrada para recibir el ID de la actividad
  @Input() actividadId: number = 0;

  // Constructor que inyecta los servicios ActivatedRoute, ReservaService y Router
  constructor(
    private route: ActivatedRoute,
    private reservaService: ReservaService,
    private router: Router
  ) {}

  /**
   * Método que se ejecuta al inicializar el componente
   */
  ngOnInit() {
    this.cargarReservas(); // Carga las reservas al inicializar el componente
  }

  /**
   * Método para cargar las reservas de una actividad específica
   */
  cargarReservas() {
    // Llama al servicio de reservas para obtener las reservas de la actividad
    this.reservaService.getReservasByActividad(this.actividadId).subscribe({
      // Maneja la respuesta en caso de éxito
      next: (data) => {
        this.reservas = data; // Almacena las reservas en la propiedad reservas
      },
      // Maneja la respuesta en caso de error
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
    // Pide confirmación antes de cancelar la reserva
    if (confirm('¿Estás seguro de que deseas cancelar esta reserva?')) {
      // Llama al servicio de reservas para cancelar la reserva
      this.reservaService.cancelarReserva(id).subscribe({
        // Maneja la respuesta en caso de éxito
        next: () => {
          this.cargarReservas(); // Recarga las reservas después de cancelar
        },
        // Maneja la respuesta en caso de error
        error: (error) => {
          // Manejo de errores (actualmente vacío)
        },
      });
    }
  }
}
