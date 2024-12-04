import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  private apiUrlPurchase = 'http://localhost:8080/api/purchase'; // Adjust the URL as needed

  constructor(private http: HttpClient) { }

  // Method to get purchase count by customer ID
  getPurchaseCountByCustomerId(customerId: number): Observable<number> {
    const url = `${this.apiUrlPurchase}/customer/${customerId}/count`;
    return this.http.get<number>(url);
  }

  getPurchasesByCustomerId(customerId: number): Observable<any> {
    return this.http.get(`${this.apiUrlPurchase}/customer/${customerId}`);
  }
}
