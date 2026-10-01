import { Routes } from '@angular/router';

import { Auth } from './components/auth/auth';
import { HomeAdmin } from './components/home-admin/home-admin';
import { Landing } from './components/landing/landing';
import { Catalogo } from './components/catalogo/catalogo';
import { authGuard, adminGuard } from './services/auth.guard';
import { FichaAnimal } from './components/ficha-animal/ficha-animal';
import { AdminAnimales } from './components/admin-animales/admin-animales';
import { MisSolicitudes } from './components/mis-solicitudes/mis-solicitudes';
import { AdminSolicitudes } from './components/admin-solicitudes/admin-solicitudes';
import { AdminSanitario } from './components/admin-sanitario/admin-sanitario';
import { HomeAdoptante } from './components/home-adoptante/home-adoptante';

export const routes: Routes = [
  { path: '', component: Landing },
  { path: 'auth', component: Auth },
  { path: 'home-admin', component: HomeAdmin, canActivate: [adminGuard] },
  { path: 'catalogo', component: Catalogo },
  { path: 'animal/:id', component: FichaAnimal },
  { path: 'admin/animales', component: AdminAnimales, canActivate: [adminGuard] },
  { path: 'admin/solicitudes', component: AdminSolicitudes, canActivate: [adminGuard] },
  { path: 'admin/sanitario', component: AdminSanitario, canActivate: [adminGuard] },
  { path: 'mis-solicitudes', component: MisSolicitudes, canActivate: [authGuard] },
  { path: 'home-adoptante', component: HomeAdoptante, canActivate: [authGuard] },
  { path: '**', redirectTo: '/' },
];
