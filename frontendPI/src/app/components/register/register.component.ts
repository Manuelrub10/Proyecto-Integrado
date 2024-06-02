import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegisterService } from '../../services/register.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  // Datos del formulario de registro inicializados con valores vacíos
  registerData = {
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    direccion: '',
    telefono: '',
    role: 'consumidor', // Valor por defecto para el rol del usuario
  };

  // Mensaje de error para mostrar en la UI en caso de fallo en el registro
  errorMessage: string = '';

  // Constructor que inyecta el servicio RegisterService y el servicio Router
  constructor(
    private registerService: RegisterService,
    private router: Router
  ) {}

  /**
   * Método para registrar un nuevo usuario
   */
  register() {
    // Llama al servicio de registro con los datos del formulario
    this.registerService.register(this.registerData).subscribe({
      // Maneja la respuesta en caso de éxito
      next: (response) => {
        if (response.status === 201) {
          // Navega a la página de login si el registro fue exitoso
          this.router.navigate(['/login']);
        } else {
          // Muestra un mensaje de error si el registro no fue exitoso
          this.errorMessage =
            'Error en el registro: ' +
            (response.body?.message || 'Unknown error');
        }
      },
      // Maneja la respuesta en caso de error
      error: (error) => {
        this.errorMessage =
          'Error en el registro: El correo electrónico ya está registrado';
      },
    });
  }

  /**
   * Método para navegar a la página de login
   */
  navegateLogin() {
    this.router.navigate(['login']);
  }
}
