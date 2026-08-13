import { Routes } from '@angular/router';

import { Auth } from './components/auth/auth';
import { HomeAdmin } from './components/home-admin/home-admin';
import { HomeAdoptante } from './components/home-adoptante/home-adoptante';
import { Catalogo } from './components/catalogo/catalogo';
import { authGuard, adminGuard } from './services/auth.guard';
import { FichaAnimal } from './components/ficha-animal/ficha-animal';
import { AdminAnimales } from './components/admin-animales/admin-animales';

export const routes: Routes = [
  { path: '', redirectTo: '/auth', pathMatch: 'full' },
  { path: 'auth', component: Auth },
  { path: 'home-admin', component: HomeAdmin, canActivate: [adminGuard] },
  { path: 'home-adoptante', component: HomeAdoptante, canActivate: [authGuard] },
  { path: 'catalogo', component: Catalogo },                                    // público
  { path: 'animal/:id', component: FichaAnimal },                               // público (ficha)
  { path: 'admin/animales', component: AdminAnimales, canActivate: [adminGuard] },  // solo admin
  { path: '**', redirectTo: '/auth' },
];
