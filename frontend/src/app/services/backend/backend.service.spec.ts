import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private apiUrl = 'http://localhost:8080/auth/check'; // Replace with your API URL

  constructor(private http: HttpClient) { }

  checkBackend(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }
}