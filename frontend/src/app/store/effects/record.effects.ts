import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {interval, of, startWith} from 'rxjs';
import { catchError, map, mergeMap, switchMap } from 'rxjs/operators';
import * as RecordActions from '../actions/record.actions';
import { HttpClient } from '@angular/common/http';
import {ApiService} from "../../services/api/api.service";
import {VehicleData} from "../../interfaces/VehicleData";
import {API_ENDPOINTS} from "../../constants/api-endpoints";

@Injectable()
export class RecordEffects {
    constructor(private actions$: Actions, private http: HttpClient, private apiService:ApiService) {}

    loadRecords$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RecordActions.loadRecords),
            mergeMap(() =>
                this.http.get<VehicleData[]>('/assets/records-mock-data.json').pipe(
                    map((records) => RecordActions.loadRecordsSuccess({ records })),
                    catchError(error => of(RecordActions.loadRecordsFailure({ error })))
                )
            )
        )
    );

  checkBackendConnection$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RecordActions.checkBackendConnection),
      switchMap(() =>
        interval(300000).pipe( // 5 minutes interval
          startWith(0), // Immediately execute the check
          switchMap(() =>
            this.apiService.getStatus(API_ENDPOINTS.API_HEALTH).pipe(
              map(() => RecordActions.backendConnectionSuccess()),
              catchError(() => of(RecordActions.backendConnectionFailure()))
            )
          )
        )
      )
    )
  );
}
