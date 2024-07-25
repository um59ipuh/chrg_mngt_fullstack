import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { RouterModule } from '@angular/router';
import { StoreModule } from '@ngrx/store';
import {MatCardHeader, MatCardModule, MatCardSubtitle, MatCardTitle} from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { EffectsModule } from '@ngrx/effects';
import {recordReducer} from "../../store/reducers/record.reducer";
import {RecordEffects} from "../../store/effects/record.effects";

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatSnackBarModule,
    StoreModule.forFeature('records', recordReducer),
    EffectsModule.forFeature([RecordEffects]),
    MatCardTitle,
    MatCardSubtitle,
    MatCardHeader
  ],
  exports: [
    HomeComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class HomeModule { }
