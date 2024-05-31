import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeConsumidorComponent } from './components/home-consumidor/home-consumidor.component';
import { HomeOfertanteComponent } from './components/home-ofertante/home-ofertante.component';
import { CrearActividadComponent } from './components/crear-actividad/crear-actividad.component';
import { EditarActividadComponent } from './components/editar-actividad/editar-actividad.component';
import { GestionReservasComponent } from './components/gestion-reservas/gestion-reservas.component';
import { ConsumidorPerfilComponent } from './components/consumidor-perfil/consumidor-perfil.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'consumidor-perfil/:id',
    component: ConsumidorPerfilComponent,
  },
  {
    path: 'actividad/:id/gestion-reservas',
    component: GestionReservasComponent,
  },
  {
    path: 'home-consumidor',
    component: HomeConsumidorComponent,
  },
  {
    path: 'home-ofertante',
    component: HomeOfertanteComponent,
  },
  {
    path: 'crear-actividad',
    component: CrearActividadComponent,
  },
  {
    path: 'editar-actividad/:id',
    component: EditarActividadComponent,
  },
];
