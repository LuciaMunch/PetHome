import { Routes } from '@angular/router';

import { Auth } from './components/auth/auth';
import { HomeAdmin } from './components/home-admin/home-admin';
import { HomeAdoptante } from './components/home-adoptante/home-adoptante';
import { Catalogo } from './components/catalogo/catalogo';
import { authGuard, adminGuard } from './services/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/auth', pathMatch: 'full' },
  { path: 'auth', component: Auth },
  { path: 'home-admin', component: HomeAdmin, canActivate: [adminGuard] },
  { path: 'home-adoptante', component: HomeAdoptante, canActivate: [authGuard] },
  { path: 'catalogo', component: Catalogo, canActivate: [authGuard] },
  { path: '**', redirectTo: '/auth' }
];
