import { createFeatureSelector, createSelector } from '@ngrx/store';
import { State } from '../reducers/record.reducer';

export const selectRecordState = createFeatureSelector<State>('records');

export const selectRecords = createSelector(
    selectRecordState,
    (state: State) => state.records
);

export const selectRecordById = (id: number)  => createSelector(
    selectRecords, records => records.find(record => record.sessionId == id)
);

export const selectIsBackendConnected = createSelector(
  selectRecordState,
  (state: State) => state.isBackendConnected
);
