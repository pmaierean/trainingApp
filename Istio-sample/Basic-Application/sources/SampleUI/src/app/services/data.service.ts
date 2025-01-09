import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IDataCollectionResponse} from '../store/store.definitions';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  constructor(private readonly httpClient: HttpClient) { }

  getAll(): Observable<IDataCollectionResponse> {
    return this.httpClient.get<IDataCollectionResponse>('http://localhost:8080/v1/api/data');
  }
}
