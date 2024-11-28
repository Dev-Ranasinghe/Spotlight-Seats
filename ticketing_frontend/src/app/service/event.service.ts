import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Event, EventDTO } from '../model/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private apiUrlEvent ='http://localhost:8080/api/event'; 

  constructor(private http: HttpClient) { }

  createEvent(event: EventDTO) : Observable<Event>{
    return this.http.post<Event>(this.apiUrlEvent, event);
  }

  getEventsByVendorId(vendorId: number): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrlEvent}/vendor/${vendorId}`);
  }

  // Method to get active events
  getActiveEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrlEvent}/total-events/active`);
  }

  deleteEvent(eventId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrlEvent}/${eventId}`);
  }  
  
  activateEvent(eventId: number): Observable<string> {
    return this.http.post(`${this.apiUrlEvent}/${eventId}/activate`, null, {
      responseType: 'text', // Specify plain text response type
    });
  }  

  isEventActive(eventId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrlEvent}/${eventId}/isActive`);
  }
}