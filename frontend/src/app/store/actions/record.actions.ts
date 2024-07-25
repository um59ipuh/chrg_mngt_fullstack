import { createAction, props } from '@ngrx/store';
import {VehicleData} from "../../interfaces/VehicleData";

export const loadRecords = createAction('[VehicleData] Load VehicleData');
export const loadRecordsSuccess = createAction('[VehicleData] Load Records Success', props<{ records: VehicleData[] }>());
export const loadRecordsFailure = createAction('[VehicleData] Load Records Failure', props<{ error: any }>());

export const addRecord = createAction('[VehicleData] Add Record', props<{ record: VehicleData }>());
export const addRecordSuccess = createAction('[VehicleData] Add Record Success', props<{ record: VehicleData }>());
export const addRecordFailure = createAction('[VehicleData] Add Record Failure', props<{ error: any }>());

export const checkBackendConnection = createAction('[boolean] Check Backend Connection');
export const backendConnectionSuccess = createAction('[boolean] Backend Connection Success');
export const backendConnectionFailure = createAction('[boolean] Backend Connection Failure');
