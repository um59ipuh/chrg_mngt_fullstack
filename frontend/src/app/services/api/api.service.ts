import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { VehicleData } from '../../interfaces/VehicleData';
import { State } from '../../store/reducers/record.reducer';
import { selectIsBackendConnected } from '../../store/selectors/record.selectors';
import { checkBackendConnection } from '../../store/actions/record.actions';

@Injectable({
  providedIn: 'root',
})
export class ApiService implements OnInit {
  isBackendConnected$: Observable<boolean>;
  isBackendRunning: boolean = false;

  private readonly tokenKey = 'token';
  private readonly baseUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient, private store: Store<State>) {
    this.isBackendConnected$ = this.store.select(selectIsBackendConnected);
  }

  ngOnInit(): void {
    this.isBackendConnected$.subscribe((isBackendConnected) => {
      this.isBackendRunning = isBackendConnected;
    });
  }

  /**
   * Fetch records from the given endpoint with optional parameters.
   * @param params Query parameters to include in the request.
   * @param endpoint The endpoint to fetch the records from.
   * @returns Observable with the fetched records and pagination details.
   */
  getRecords(params: HttpParams, endpoint: string): Observable<{ content: VehicleData[]; page: { totalElements: number } }> {
    return this.http.get<{ content: VehicleData[]; page: { totalElements: number } }>(`${this.baseUrl}/${endpoint}`, { params });
  }

  /**
   * Fetch a single record from the given endpoint.
   * @param endpoint The endpoint to fetch the record from.
   * @returns Observable with the fetched record.
   */
  getRecord(endpoint: string): Observable<VehicleData> {
    return this.http.get<VehicleData>(`${this.baseUrl}/${endpoint}`);
  }

  /**
   * Fetch the status from the given endpoint.
   * @param endpoint The endpoint to fetch the status from.
   * @returns Observable with the status.
   */
  getStatus(endpoint: string): Observable<{ status: string }> {
    return this.http.get<{ status: string }>(`${this.baseUrl}/${endpoint}`);
  }

  /**
   * Send a POST request to the given endpoint with the provided data.
   * @param endpoint The endpoint to send the data to.
   * @param data The data to send.
   * @returns Observable with the response.
   */
  postData(endpoint: string, data: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post<any>(`${this.baseUrl}/${endpoint}`, data, { headers });
  }

  /**
   * Set the authentication token in local storage.
   * @param token The token to store.
   */
  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  /**
   * Get the authentication token from local storage.
   * @returns The stored token or null if not present.
   */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  /**
   * Remove the authentication token from local storage.
   */
  removeToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  /**
   * Check if the user is authenticated.
   * @returns True if the token is present, otherwise false.
   */
  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }
}
