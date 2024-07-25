import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthComponent } from './auth/auth.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';  // <-- Import FormsModule and ReactiveFormsModule

const routes: Routes = [
  { path: '', component:AuthComponent },
 ];

@NgModule({
  declarations: [
    AuthComponent,

  ],
  imports: [
    CommonModule,
    FormsModule,ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class AuthModule { }
