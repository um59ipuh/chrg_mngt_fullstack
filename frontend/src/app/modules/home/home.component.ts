import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import {selectIsBackendConnected} from "../../store/selectors/record.selectors";
import {
  backendConnectionFailure,
  backendConnectionSuccess,
  checkBackendConnection
} from "../../store/actions/record.actions";
import {ApiService} from "../../services/api/api.service";
import {API_ENDPOINTS} from "../../constants/api-endpoints";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isBackendConnected$: Observable<boolean>;

  constructor(private store: Store, private router: Router, private snackBar: MatSnackBar, private apiService: ApiService) {
    this.isBackendConnected$ = this.store.select(selectIsBackendConnected);
  }

  ngOnInit() {
    this.initCons();
  }

  initCons() {
    this.apiService.getStatus(API_ENDPOINTS.API_HEALTH).subscribe(res => {
      if (res.status === "ok") {
        this.snackBar.open('Backend is up and running again', 'Close', {
          duration: 3000,
        });
        // update state for further communication
        this.store.dispatch(backendConnectionSuccess());
        // now redirect to /api if already not authenticated
        if (this.apiService.isAuthenticated()) {
          this.router.navigate(['/record'])
        } else {
          this.router.navigate(['/auth'])
        }
      }
    }, error => {
      this.snackBar.open('Backend is not up and running. Redirecting to local Mock storage.', 'Close', {
        duration: 3000,
      });
      this.store.dispatch(backendConnectionFailure());
      this.router.navigate(['/record'])
    });
  }
}
