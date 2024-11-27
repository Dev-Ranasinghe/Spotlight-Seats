import { Injectable } from '@angular/core';
import { User } from '../model/user.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Customer } from '../model/customer.model';
import { Observable } from 'rxjs';
import { Vendor } from '../model/vendor.model';

@Injectable({
  providedIn: 'root'
})

export class LoginService {

  private apiUrlCustomer ='http://localhost:8080/api/customer'; 
  private apiUrlVendor ='http://localhost:8080/api/vendor'; 

  constructor(private http: HttpClient) { }

  loginCustomer(user: User){

    // Set up the HTTP params for the GET request
    const params = new HttpParams()
      .set('username', user.username)
      .set('password', user.password);

    // Send a GET request with the params
    return this.http.get<boolean>(this.apiUrlCustomer + '/login', {params});
  }

  loginVendor(user: User){
    // Set up the HTTP params for the GET request
    const params = new HttpParams()
      .set('username', user.username)
      .set('password', user.password);

    // Send a GET request with the params
    return this.http.get<boolean>(this.apiUrlVendor + '/login', {params});
  }

  loggedInUser: User={
    username: '',
    password: ''
  }

  signUpCustomer(customer: Customer) : Observable<Customer>{
    return this.http.post<Customer>(this.apiUrlCustomer, customer);
  }

  signUpVendor(vendor: Vendor) : Observable<Vendor>{
    return this.http.post<Vendor>(this.apiUrlVendor, vendor);
  }
}