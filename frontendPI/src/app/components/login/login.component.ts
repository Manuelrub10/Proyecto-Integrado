import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  // Propiedades del componente para almacenar los datos del formulario de login
  email: string = '';
  password: string = '';
  role: string = 'consumidor'; // Valor por defecto para el rol del usuario
  errorMessage: string = ''; // Propiedad para almacenar el mensaje de error

  // Constructor que inyecta los servicios LoginService y Router
  constructor(private loginService: LoginService, private router: Router) {}

  /**
   * Método para iniciar sesión
   */
  login() {
    // Llama al servicio de login con los datos del formulario
    this.loginService.login(this.email, this.password, this.role).subscribe({
      // Maneja la respuesta en caso de éxito
      next: (data) => {
        if (this.role === 'consumidor') {
          this.router.navigate(['/home-consumidor']); // Navega a la página de inicio del consumidor
        } else if (this.role === 'ofertante') {
          this.router.navigate(['/home-ofertante']); // Navega a la página de inicio del ofertante
        }
      },
      // Maneja la respuesta en caso de error
      error: (error) => {
        this.errorMessage = 'Correo electrónico o contraseña incorrectos'; // Muestra un mensaje de error
      },
    });
  }

  /**
   * Método para navegar a la página de registro
   */
  navigateRegister() {
    this.router.navigate(['register']); // Navega a la página de registro
  }
}
