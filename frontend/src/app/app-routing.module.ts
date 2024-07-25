import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './services/auth.guard';
import {HomeComponent} from "./modules/home/home.component";
import {UI_ENDPOINTS} from "./constants/ui-endpoints";

const routes: Routes = [
  { path: UI_ENDPOINTS.AUTH, loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule) },
  { path: UI_ENDPOINTS.RECORD, loadChildren: () => import('./modules/record/record.module').then(m => m.RecordModule), canActivate: [authGuard] },
  { path: UI_ENDPOINTS.HOME, component: HomeComponent },
  { path: '', redirectTo: `/${UI_ENDPOINTS.HOME}`, pathMatch: 'full' },
 ];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
