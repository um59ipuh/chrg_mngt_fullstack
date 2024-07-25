import { createReducer, on } from '@ngrx/store';
import * as RecordActions from '../actions/record.actions';
import {VehicleData} from "../../interfaces/VehicleData";

export interface State {
    records: VehicleData[];
    error: any;
    isBackendConnected: boolean;
}

export const initialState: State = {
    records: [],
    error: null,
    isBackendConnected: false,
};

export const recordReducer = createReducer(
    initialState,
    on(RecordActions.loadRecordsSuccess, (state, { records }) => ({ ...state, records })),
    on(RecordActions.loadRecordsFailure, (state, { error }) => ({ ...state, error })),
    on(RecordActions.addRecord, (state, { record }) => ({ ...state, records: [...state.records, record] })),
    on(RecordActions.addRecordFailure, (state, { error }) => ({ ...state, error })),
    on(RecordActions.backendConnectionSuccess, (state) => ({ ...state, isBackendConnected: true })),
    on(RecordActions.backendConnectionFailure, (state) => ({ ...state, isBackendConnected: false }))
);
