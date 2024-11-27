import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../model/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrlVendor ='http://localhost:8080/api/customer'; 

  constructor(private http: HttpClient) { }

  getCustomerByEmail(username: string): Observable<Customer> {
    // Directly include the email in the URL path to match the backend's @PathVariable setup
    return this.http.get<Customer>(`${this.apiUrlVendor}/email/${username}`);
  }
  
  // Method to get all vendors
  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrlVendor}`);
  }
}
