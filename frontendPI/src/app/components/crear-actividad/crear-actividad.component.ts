import { Component, OnInit } from '@angular/core';
import { ActividadService } from '../../services/actividad.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { format } from 'date-fns';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-crear-actividad',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './crear-actividad.component.html',
  styleUrls: ['./crear-actividad.component.css'],
})
export class CrearActividadComponent implements OnInit {
  // Objeto para almacenar los datos de la nueva actividad
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
    ofertanteId: null, // ID del ofertante se establece más adelante
  };

  // Fecha mínima permitida para la actividad
  minDate: string = '';

  // Constructor que inyecta los servicios ActividadService, Router y LoginService
  constructor(
    private actividadService: ActividadService,
    private router: Router,
    private loginService: LoginService
  ) {}

  /**
   * Método que se ejecuta al inicializar el componente
   */
  ngOnInit() {
    // Obtiene el usuario actual del servicio de login
    const currentUser = this.loginService.valorUsuarioActual;
    if (currentUser && currentUser.usuario && currentUser.usuario.ofertante) {
      this.actividad.ofertanteId = currentUser.usuario.ofertante.id; // Establece el ID del ofertante
    } else {
      this.router.navigate(['/login']); // Navega a la página de login si el usuario no es válido
    }

    // Establece la fecha mínima permitida para la actividad
    const now = new Date();
    this.minDate = now.toISOString().slice(0, 16);
  }

  /**
   * Método para crear una nueva actividad
   */
  crearActividad() {
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

    // Validación de fecha pasada
    const fechaActividad = new Date(this.actividad.fecha);
    const now = new Date();
    if (fechaActividad < now) {
      alert('La fecha de la actividad no puede ser una fecha pasada.');
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

    // Llama al servicio de actividades para crear una nueva actividad
    this.actividadService.crearActividad(actividadData).subscribe({
      // Maneja la respuesta en caso de éxito
      next: (response) => {
        this.router.navigate(['/home-ofertante']); // Navega a la página de inicio del ofertante
      },
      // Maneja la respuesta en caso de error
      error: (error) => {
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
