import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse,
  HttpClient
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {

  constructor(private http: HttpClient, private apiService:ApiService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.apiService.getToken();

    let clonedReq = req;
    if (!req.url.includes("/login")) {
      if (token) {
        clonedReq = req.clone({
          headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
      }
    }

    return next.handle(clonedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Token expired or invalid, try refreshing the token
          return this.handle401Error(clonedReq, next);
        } else {
          return throwError(error);
        }
      })
    );
  }

  private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.apiService.postData('refreshtoken', {}).pipe(
      switchMap((data: any) => {
        // Assume the new token is returned in the 'token' field
        const newToken = data.token;
        this.apiService.setToken(newToken);
        // Clone the request with the new token
        const clonedReq = req.clone({
          headers: req.headers.set('Authorization', `Bearer ${newToken}`)
        });

        // Retry the request with the new token
        return next.handle(clonedReq);
      }),
      catchError((err) => {
        return throwError(err);
      })
    );
  }
}
