import {NgModule, isDevMode, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { InterceptorService } from './services/interceptor/interceptor.service';
import { HomeComponent } from './modules/home/home.component';
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { recordReducer } from './store/reducers/record.reducer';
import { RecordEffects } from './store/effects/record.effects';
import {reduce} from "rxjs";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {HomeModule} from "./modules/home/home.module";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule, FormsModule, ReactiveFormsModule,
    AppRoutingModule, MatSnackBarModule, MatTableModule, BrowserAnimationsModule,
    HttpClientModule, MatCard, MatCardContent,
    MatToolbarModule, MatInputModule, MatIconModule, MatButtonModule,
    StoreModule.forRoot({records: recordReducer}),
    EffectsModule.forRoot([RecordEffects,]),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: false}),
    // StoreModule.forRoot({}, {}),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: !isDevMode()}),
    HomeModule, MatCardTitle, MatCardHeader
  ],
  providers: [
    provideAnimationsAsync(),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }
