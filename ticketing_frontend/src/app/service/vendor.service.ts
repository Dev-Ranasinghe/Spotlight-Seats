import { Injectable } from '@angular/core';
import { User } from '../model/user.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Vendor } from '../model/vendor.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VendorService {

  private apiUrlVendor ='http://localhost:8080/api/vendor'; 

  constructor(private http: HttpClient) { }

  getVendorByEmail(username: string): Observable<Vendor> {
    // Directly include the email in the URL path to match the backend's @PathVariable setup
    return this.http.get<Vendor>(`${this.apiUrlVendor}/email/${username}`);
  }

  // Method to get all vendors
  getAllVendors(): Observable<Vendor[]> {
    return this.http.get<Vendor[]>(`${this.apiUrlVendor}`);
  }

  // Method to delete a vendor by ID
  deleteVendor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrlVendor}/${id}`);
  }

  // method to get total tickets for each vendor
  getTotalTicketsByVendor(vendorId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrlVendor}/${vendorId}/totalTickets`);
  }

  // method to get all the vendor emails
  getAllVendorEmails(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrlVendor}/emails`);
  }
  
}