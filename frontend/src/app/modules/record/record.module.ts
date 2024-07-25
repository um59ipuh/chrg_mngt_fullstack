import { NgModule } from '@angular/core';
import { CommonModule,DatePipe } from '@angular/common';
import { RecordComponent } from './record/record.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';  // <-- Import FormsModule and ReactiveFormsModule
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { HttpClientModule } from '@angular/common/http';
import { CreateRecordDialogComponent } from './create-record-dialog/create-record-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
const routes: Routes = [
  { path: '', component:RecordComponent },
 ];

@NgModule({
  declarations: [
    RecordComponent,
    CreateRecordDialogComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MatTableModule, MatSortModule,
    FormsModule, ReactiveFormsModule,
    MatPaginatorModule, HttpClientModule, DatePipe,
    MatDialogModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatToolbar, MatIcon,

  ]
})
export class RecordModule { }
