import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private apiUrlConfig ='http://localhost:8080/api/config'; 

  constructor(private http: HttpClient) { }

  getTotalTickets(): Observable<string> {
    return this.http.get<string>(`${this.apiUrlConfig}/total-tickets`);
  }

  getMaxCapacity(): Observable<string> {
    return this.http.get<string>(`${this.apiUrlConfig}/max-tickets`);
  }

  getAvailableTickets(): Observable<number> {
    return this.http.get<number>(`${this.apiUrlConfig}/available-tickets`);
  }

  getCustomerRetrievalRate(): Observable<string> {
    return this.http.get<string>(`${this.apiUrlConfig}/customer-retrieval-rate`);
  }

  getTicketReleaseRate(): Observable<string> {
    return this.http.get<string>(`${this.apiUrlConfig}/ticket-release-rate`);
  }

  updateSystemParameter(key: string, value: string): Observable<void> {
    const params = new HttpParams()
      .set('key', key)
      .set('value', value);

    return this.http.put<void>(`${this.apiUrlConfig}`, null, { params });
  }  
}