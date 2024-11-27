import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { LoginService } from '../service/login.service';
import { User } from '../model/user.model';
import { Customer } from '../model/customer.model';
import { Router } from '@angular/router';
import { Vendor } from '../model/vendor.model';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {

  signInData = {
    userType: '',
    password: '',
    username: ''
  };

  signUpData = {
    userType: '',
    password: '',
    repeatPassword: '',
    username: '',
    name: '',
    contact: ''
  };

constructor(private loginService: LoginService, private router: Router) { }

  onSignIn(){
      let user:User = {
        username: this.signInData.username,
        password: this.signInData.password
      }

      if(this.signInData.userType === "Customer"){
        this.loginService.loginCustomer(user).subscribe((validUser: boolean) => {
          if(validUser){
            this.router.navigate(['/customer-dashboard']);
            this.loginService.loggedInUser.username = user.username;
          }
        });   
    }

      if(this.signInData.userType === "Vendor"){
        this.loginService.loginVendor(user).subscribe((validUser: boolean) => {
          if(validUser){
            this.router.navigate(['/vendor-dashboard']);
            this.loginService.loggedInUser.username = user.username;
          }
        });   
    }

    if(this.signInData.userType === "Admin" && this.signInData.username === "admin" && this.signInData.password === "123"){
      this.router.navigate(['/admin-dashboard'])
    }
  }

  onSignUp(){

    if(this.signUpData.userType === "Customer"){
      let customer:Customer = {
        customerName: this.signUpData.name,
        customerContact: this.signUpData.contact,
        customerPriority: false,
        customerPassword: this.signUpData.password,
        customerEmail: this.signUpData.username,
        customerId: -1
      }
      this.loginService.signUpCustomer(customer).subscribe((data: Customer) => {
        console.log(data);

        });
    } 
    else{
      let vendor:Vendor = {
        vendorName: this.signUpData.name,
        vendorContact: this.signUpData.contact,
        vendorPassword: this.signUpData.password,
        vendorEmail: this.signUpData.username,
        vendorId: -1
      }
      this.loginService.signUpVendor(vendor).subscribe((data: Vendor) => {
        console.log(data);

        });
    }   
  }
}
