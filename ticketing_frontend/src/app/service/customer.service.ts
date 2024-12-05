import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../model/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrlCustomer ='http://localhost:8080/api/customer'; 

  constructor(private http: HttpClient) { }

  getCustomerByEmail(username: string): Observable<Customer> {
    // Directly include the email in the URL path to match the backend's @PathVariable setup
    return this.http.get<Customer>(`${this.apiUrlCustomer}/email/${username}`);
  }
  
  // Method to get all vendors
  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrlCustomer}`);
  }

  // Method to delete a vendor by ID
  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrlCustomer}/${id}`);
  }

  // Method to update customer priority to true
  updateCustomerPriorityToTrue(id: number): Observable<void> {
    const url = `${this.apiUrlCustomer}/${id}/priority`;
    return this.http.put<void>(url, null); 
  }

  // method to get all the customers
  getAllCustomerEmails(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrlCustomer}/emails`);
  }
}
