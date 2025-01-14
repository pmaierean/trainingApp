import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IData, IDataCollectionResponse} from '../store/store.definitions';
import {Observable} from 'rxjs';
import { Environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})

export class DataService {
  constructor(private readonly httpClient: HttpClient) { }

  getAll(): Observable<IDataCollectionResponse> {
    return this.httpClient.get<IDataCollectionResponse>(Environment.backendUrl + '/v1/api/data');
  }

  addElement(element: IData): Observable<IDataCollectionResponse> {
    return this.changeElement("ADD", element);
  }

  removeElement(element: IData): Observable<IDataCollectionResponse> {
    return this.changeElement("REMOVE", element);
  }

  updateElement(element: IData): Observable<IDataCollectionResponse> {
    return this.changeElement("UPDATE", element);
  }

  private changeElement(operation: string, element: IData): Observable<IDataCollectionResponse> {
    return this.httpClient.post<IDataCollectionResponse>(Environment.backendUrl + '/v1/api/data', {
      operation: operation,
      data: element
    });
  }

}
