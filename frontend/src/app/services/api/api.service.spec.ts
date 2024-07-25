import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApiService } from './api.service';
import { StoreModule, Store } from '@ngrx/store';
import { State } from '../../store/reducers/record.reducer';
import {HttpParams} from "@angular/common/http";
import {VehicleData} from "../../interfaces/VehicleData";

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;
  let store: Store<State>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, StoreModule.forRoot({})],
      providers: [ApiService]
    });
    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
    store = TestBed.inject(Store);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch records', () => {
    const dummyData = { content: [], page: { totalElements: 0 } };
    let params = new HttpParams()
      .set('page', '0')
      .set('size', '10')
      .set('sort', 'sessionId' + "," + 'DESC');
    service.getRecords(params, 'records').subscribe(data => {
      expect(data).toEqual(dummyData);
    });
    const req = httpMock.expectOne('http://localhost:8080/records');
    expect(req.request.method).toBe('GET');
    req.flush(dummyData);
  });

  it('should fetch a single record', () => {
    const dummyRecord = {} as VehicleData;
    service.getRecord('record/1').subscribe(data => {
      expect(data).toEqual(dummyRecord);
    });
    const req = httpMock.expectOne('http://localhost:8080/record/1');
    expect(req.request.method).toBe('GET');
    req.flush(dummyRecord);
  });

  it('should post data', () => {
    const dummyResponse = { success: true };
    service.postData('post', { data: 'test' }).subscribe(response => {
      expect(response).toEqual(dummyResponse);
    });
    const req = httpMock.expectOne('http://localhost:8080/post');
    expect(req.request.method).toBe('POST');
    req.flush(dummyResponse);
  });

  it('should set, get, and remove token', () => {
    service.setToken('12345');
    expect(service.getToken()).toBe('12345');
    service.removeToken();
    expect(service.getToken()).toBeNull();
  });

  it('should check authentication', () => {
    service.setToken('12345');
    expect(service.isAuthenticated()).toBeTrue();
    service.removeToken();
    expect(service.isAuthenticated()).toBeFalse();
  });
});
