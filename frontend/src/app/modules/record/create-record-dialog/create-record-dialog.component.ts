import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { VehicleData } from '../../../interfaces/VehicleData';
import { selectRecords } from '../../../store/selectors/record.selectors';
import { State } from '../../../store/reducers/record.reducer';
import { loadRecords, addRecord } from '../../../store/actions/record.actions';
import { MatSnackBar } from '@angular/material/snack-bar';
import {ApiService} from "../../../services/api/api.service";
import {NotificationService} from "../../../services/notification/notification.service";

@Component({
  selector: 'app-create-record-dialog',
  templateUrl: './create-record-dialog.component.html',
  styleUrls: ['./create-record-dialog.component.css']
})
export class CreateRecordDialogComponent implements OnInit {
  form: FormGroup;
  records$: Observable<VehicleData[]>;
  mockDataSource: VehicleData[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CreateRecordDialogComponent>,
    private store: Store<State>,
    private snackBar: MatSnackBar,
    private apiService: ApiService,
    private notificationService: NotificationService,
  ) {
    const today = new Date().toISOString().split('T')[0];
    this.form = this.fb.group({
      vehicleId: ['', Validators.required],
      startTime: [`${today}T00:00:00`, Validators.required],
      endTime: [`${today}T00:00:00`, [Validators.required]],
      totalCost: ['', [Validators.required, Validators.min(1)]]
    }, { validators: this.startTimeEndTimeValidator });
    this.records$ = this.store.select(selectRecords);
  }

  ngOnInit(): void {
    this.store.dispatch(loadRecords());
    this.loadMockDataFromState();
  }


  /**
   * Load mock data from the state.
   */
  private loadMockDataFromState(): void {
    this.records$.subscribe(records => {
      this.mockDataSource = [...records];
    });
  }

  /**
   * Handle form submission.
   */
  onSubmit(): void {
    if (this.form.valid) {
      const latestEntry = this.getLatestEntry();
      const newRecord: VehicleData = {
        ...this.form.value,
        sessionId: latestEntry.sessionId + 1
      };

      if (this.isRecordValid(newRecord, latestEntry)) {
        this.dialogRef.close(newRecord);
      } else {
        this.showSnackBar('Start time must be greater than the latest end time for this vehicle.');
      }
    }
  }

  /**
   * Close the dialog without saving.
   */
  onCancel(): void {
    this.dialogRef.close();
  }

  /**
   * Get the latest record entry based on end time.
   */
  private getLatestEntry(): VehicleData {
    return this.mockDataSource.sort((a, b) => new Date(b.endTime).getTime() - new Date(a.endTime).getTime())[0];
  }

  /**
   * Custom validator to ensure end time is after start time.
   */
  private startTimeEndTimeValidator(group: AbstractControl): { [key: string]: boolean } | null {
    const startTime = group.get('startTime')?.value;
    const endTime = group.get('endTime')?.value;
    return new Date(endTime) > new Date(startTime) ? null : { endTimeBeforeStartTime: true };
  }

  /**
   * Validate the new record against the latest record entry.
   */
  private isRecordValid(newRecord: VehicleData, latestRecord: VehicleData): boolean {
    return new Date(newRecord.startTime) > new Date(latestRecord.endTime);
  }

  /**
   * Show a snack bar with a given message.
   */
  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Close', { duration: 3000 });
  }
}
