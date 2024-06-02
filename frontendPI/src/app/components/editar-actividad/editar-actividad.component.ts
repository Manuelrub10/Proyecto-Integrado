import { Component } from '@angular/core';
import { format } from 'date-fns';
import { ActividadService } from '../../services/actividad.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-editar-actividad',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './editar-actividad.component.html',
  styleUrl: './editar-actividad.component.css',
})
export class EditarActividadComponent {
  // Objeto para almacenar los datos de la actividad
  actividad: any = {
    titulo: '',
    descripcion: '',
    duracion: 0,
    numMaxParticipantes: 0,
    numMinParticipantes: 0,
    fecha: '',
    lugar: '',
    materialNecesario: '',
    materialOfrecido: '',
    tipo: '',
  };

  // ID de la actividad
  actividadId: number;

  // Fecha mínima permitida para la actividad
  minDate: string = '';

  // Constructor que inyecta los servicios ActividadService, ActivatedRoute y Router
  constructor(
    private actividadService: ActividadService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // Obtiene el ID de la actividad de los parámetros de la ruta
    this.actividadId = +this.route.snapshot.paramMap.get('id')!;
  }

  /**
   * Método que se ejecuta al inicializar el componente
   */
  ngOnInit(): void {
    // Llama al servicio de actividades para obtener los detalles de la actividad por su ID
    this.actividadService.obtenerActividadPorId(this.actividadId).subscribe({
      // Maneja la respuesta en caso de éxito
      next: (data) => {
        this.actividad = data; // Almacena los datos de la actividad
        this.actividad.fecha = format(
          new Date(this.actividad.fecha),
          "yyyy-MM-dd'T'HH:mm:ss"
        ); // Formatea la fecha de la actividad
      },
      // Maneja la respuesta en caso de error
      error: (error) => {
        // Manejo de errores (actualmente vacío)
      },
    });

    // Establece la fecha mínima permitida para la actividad
    const now = new Date();
    this.minDate = now.toISOString().slice(0, 16);
  }

  /**
   * Método para actualizar la actividad
   */
  actualizarActividad() {
    // Validaciones de los datos de la actividad
    if (this.actividad.duracion < 0) {
      alert('La duración no puede ser negativa.');
      return;
    }
    if (
      this.actividad.numMinParticipantes <= 0 ||
      this.actividad.numMaxParticipantes <= 0
    ) {
      alert('El número de participantes debe ser positivo.');
      return;
    }
    if (
      this.actividad.numMaxParticipantes < this.actividad.numMinParticipantes
    ) {
      alert(
        'El número máximo de participantes debe ser mayor o igual al número mínimo de participantes.'
      );
      return;
    }

    // Formatea la fecha de la actividad antes de enviarla al servidor
    const formattedFecha = format(
      new Date(this.actividad.fecha),
      "yyyy-MM-dd'T'HH:mm:ss"
    );
    const actividadData = {
      ...this.actividad,
      fecha: formattedFecha,
    };

    // Llama al servicio de actividades para actualizar la actividad
    this.actividadService
      .editarActividad(this.actividadId, actividadData)
      .subscribe({
        // Maneja la respuesta en caso de éxito
        next: (response) => {
          this.router.navigate(['/home-ofertante']); // Navega a la página de inicio del ofertante
        },
        // Maneja la respuesta en caso de error
        error: (error) => {
          alert(error.error);
          // Manejo de errores (actualmente vacío)
        },
      });
  }

  /**
   * Método para navegar a la página de inicio del ofertante
   */
  navegateHomeOfertante() {
    this.router.navigate(['/home-ofertante']);
  }
}
