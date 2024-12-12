import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TicketPoolService {

  private apiUrl = 'http://localhost:8080/api/ticket-pool'; // Base API URL

  constructor(private http: HttpClient) { }

  getReleasedTicketCount(eventId: number): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/event/${eventId}/released-tickets`);
  }  

  decrementReleasedTicketCount(eventId: number, decrementBy: number): Observable<string> {
    return this.http.patch<string>(
      `${this.apiUrl}/event/${eventId}/decrement`,
      null,
      {
        params: { decrementBy: decrementBy.toString() },
        responseType: 'text' as 'json',
      }
    );
  }

  // Method to create a purchase (decrement ticket count and create purchase record)
  createPurchase(eventId: number, customerId: number, purchaseCount: number): Observable<string> {
    const params = new HttpParams()
      .set('customerId', customerId.toString())
      .set('purchaseCount', purchaseCount.toString());

    return this.http.post<string>(`${this.apiUrl}/event/${eventId}/purchase`, null, {
      params: params,
      responseType: 'text' as 'json', 
    });
  }
}