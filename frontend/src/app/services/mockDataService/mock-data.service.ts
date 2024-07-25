import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VehicleData } from '../../interfaces/VehicleData';

@Injectable({
    providedIn: 'root'
})
export class DataService {

    private jsonUrl = 'assets/records-mock-data.json';

    constructor(private http: HttpClient) { }

    getData(): Observable<VehicleData[]> {
        return this.http.get<VehicleData[]>(this.jsonUrl);
    }
}
