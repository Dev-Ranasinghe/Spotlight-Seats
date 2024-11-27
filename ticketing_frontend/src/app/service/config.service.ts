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

  // updateSystemParameter(key: string, value: string): void {
  //   const params = new HttpParams()
  //     .set('key', key)
  //     .set('value', value);

  //   this.http.put(`${this.apiUrlConfig}`, {params});
  // }

  updateSystemParameter(key: string, value: string): void {
    const params = new HttpParams()
      .set('key', key)
      .set('value', value);
  
    this.http.put(`${this.apiUrlConfig}`, null, { params }).subscribe({
      next: () => console.log('System parameter updated successfully.'),
      error: (err) => console.error('Error updating system parameter:', err)
    });
  }  
}