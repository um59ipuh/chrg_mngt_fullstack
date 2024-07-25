import {Component, OnInit, ViewChild, ChangeDetectorRef, input} from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort, Sort } from '@angular/material/sort';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApiService } from '../../../services/api/api.service';
import { VehicleData } from '../../../interfaces/VehicleData';
import { MatDialog } from '@angular/material/dialog';
import { CreateRecordDialogComponent } from '../create-record-dialog/create-record-dialog.component';
import {DataService} from '../../../services/mockDataService/mock-data.service';
import {addRecord, checkBackendConnection, loadRecords} from '../../../store/actions/record.actions';
import {selectRecords, selectRecordById, selectIsBackendConnected} from '../../../store/selectors/record.selectors';
import {Observable} from "rxjs";
import { Store } from '@ngrx/store';
import {State} from "../../../store/reducers/record.reducer";
import {MatInput} from "@angular/material/input";
import {API_ENDPOINTS} from "../../../constants/api-endpoints";
import {MatSnackBar} from "@angular/material/snack-bar";
import {NotificationService} from "../../../services/notification/notification.service";
import {Router} from "@angular/router";
import {UI_ENDPOINTS} from "../../../constants/ui-endpoints";

@Component({
  selector: 'app-record',
  templateUrl: './record.component.html',
  styleUrl: './record.component.css'
})

export class RecordComponent implements OnInit {
  records$: Observable<VehicleData[]>;
  isBackendConnected$: Observable<boolean>;

  displayedColumns: string[] = ['sessionId', 'vehicleId', 'startTime', 'endTime', 'totalCost'];
  dataSource: MatTableDataSource<VehicleData> = new MatTableDataSource<VehicleData>();

  totalItems = 0;
  pageSize = 10;
  currentPage = 0;
  sortField = 'startTime';
  sortDirection = 'asc';
  searchValue: string = ''

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatInput) input!: MatInput;

  constructor(private apiService: ApiService, public dialog: MatDialog,
              private store: Store<State>, private cdr: ChangeDetectorRef,
              private snackBar: MatSnackBar, private notificationService: NotificationService, private router: Router) {
    this.records$ = this.store.select(selectRecords);
    this.isBackendConnected$ = this.store.select(selectIsBackendConnected)
  }

  ngOnInit(): void {
    this.store.dispatch(checkBackendConnection());
    this.loadBasedOnLogic()
  }

  loadBasedOnLogic() {
    this.isBackendConnected$.subscribe(isBackendConnected => {
      if (isBackendConnected) {
        // If backend is connected, load data from the API
        this.loadServerData();
      } else {
        // If backend is not connected, load data from mock data
        this.loadMockData();
      }
    });
    this.store.dispatch(checkBackendConnection());
  }

  loadMockData(): void {
    this.records$.subscribe(records => {
      this.dataSource = new MatTableDataSource(records);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      this.cdr.detectChanges();
    });
    this.store.dispatch(loadRecords());
  }

  loadServerData(): void {
    let params = new HttpParams()
      .set('page', this.currentPage.toString())
      .set('size', this.pageSize.toString())
      .set('sort', this.sortField + "," + this.sortDirection.toUpperCase());

    this.apiService.getRecords(params, API_ENDPOINTS.GET_RECORDS).subscribe(response => {
      this.dataSource = new MatTableDataSource(response.content);
      this.totalItems = response.page.totalElements;
    }, error => {
      if (error.status == 403) {
        this.router.navigate([`/${UI_ENDPOINTS.AUTH}`])
      }
    });
  }

  validateSearchValue(): boolean {
    if (this.searchValue.length > 0 && parseInt(this.searchValue, 10) > 0) {
      return true
    }
    return false
  }

  applyFilter(idValue: string) {
    if (this.validateSearchValue()) {
      this.isBackendConnected$.subscribe(flag => {
        if (flag) {
          this.apiService.getRecord(`${API_ENDPOINTS.GET_RECORDS_BY_ID}${idValue}`).subscribe(value => {
            this.dataSource.data = [value]
          })
        } else {
          let filetedData = this.dataSource.data.filter(record => record.sessionId.toString() === idValue);
          if(filetedData.length > 0){
            this.dataSource.data = filetedData
          } else {
            this.notificationService.showError("Invalid or Not Found!")
          }
        }
      })
    } else {
      this.notificationService.showError("Invalid or Not Found!")
    }
  }


  public onKeyDown(event: KeyboardEvent): void {
    console.log(event.key);
    const prohibitedKeys = new Set(['Backspace', 'Delete']);
    if (prohibitedKeys.has(event.key)) {
      event.preventDefault();
    }
  }


  clearSearch() {
    if (this.searchValue != null || this.searchValue !== '') {
      this.searchValue = ''
    }
    this.loadBasedOnLogic()
  }

  sortData(sort: Sort): void {
    this.sortField = sort.active;
    this.sortDirection = sort.direction;
    this.isBackendConnected$.subscribe(value => {
      if (!value) {
        this.dataSource.sort = this.sort;
      } else {
        this.loadBasedOnLogic()
      }
    });
  }

  pageEvent(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.isBackendConnected$.subscribe(value => {
      if (!value) {
        this.dataSource.paginator = this.paginator;
      } else {
        this.loadBasedOnLogic()
      }
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateRecordDialogComponent, {
      width: '400px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.addRecord(result);
      }
    });
  }

  addRecord(record: VehicleData): void {
    this.isBackendConnected$.subscribe(isBackendConnected => {
      if (isBackendConnected) {
        this.apiService.postData(API_ENDPOINTS.POST_RECORDS, record).subscribe(
          response => {
            this.loadServerData();
            this.showSnackBar('Record added successfully.');
          },
          error => {
            this.showSnackBar('Failed to add record. Please try again.');
          }
        );
      } else {
        this.store.dispatch(addRecord({record: record}));
        this.showSnackBar('Record added successfully.');
      }
    })
  }

  /**
   * Show a snack bar with a given message.
   */
  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Close', { duration: 3000 });
  }

  protected readonly event = event;
}
