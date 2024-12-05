import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { LoginService } from '../service/login.service';
import { User } from '../model/user.model';
import { Customer } from '../model/customer.model';
import { Router } from '@angular/router';
import { Vendor } from '../model/vendor.model';
import { CustomerService } from '../service/customer.service';
import { VendorService } from '../service/vendor.service';



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

constructor(private loginService: LoginService, private router: Router, private customerService: CustomerService, private vendorService: VendorService) { }

  onSignIn(form: NgForm) {
    // Clearing the local storage before sign in 
    localStorage.clear();
  
    let user: User = {
      username: this.signInData.username,
      password: this.signInData.password
    };
  
    if (this.signInData.userType === "Customer") {
      this.loginService.loginCustomer(user).subscribe({
        next: (validUser: boolean) => {
          if (validUser) {
            this.router.navigate(['/customer-dashboard']);
            this.loginService.loggedInUser.username = user.username;
          } else {
            alert('Invalid customer credentials!');
            form.resetForm();
          }
        },
        error: (err) => {
          console.error('Error during customer login:', err);
          alert('An error occurred while logging in. Please try again.');
          form.resetForm();
        }
      });
    }
  
    if (this.signInData.userType === "Vendor") {
      this.loginService.loginVendor(user).subscribe({
        next: (validUser: boolean) => {
          if (validUser) {
            this.router.navigate(['/vendor-dashboard']);
            this.loginService.loggedInUser.username = user.username;
          } else {
            alert('Invalid vendor credentials!');
            form.resetForm();
          }
        },
        error: (err) => {
          console.error('Error during vendor login:', err);
          alert('An error occurred while logging in. Please try again.');
          form.resetForm();
        }
      });
    }
  
    if (this.signInData.userType === "Admin") {
      if (this.signInData.username === "admin" && this.signInData.password === "123") {
        this.router.navigate(['/admin-dashboard']);
      } else {
        alert('Invalid admin credentials!');
        form.resetForm();
      }
    }
  }
  
  
  // onSignUp() {
    // if (this.signUpData.userType === "Customer") {
    //   let customer: Customer = {
    //     customerName: this.signUpData.name,
    //     customerContact: this.signUpData.contact,
    //     customerPriority: false,
    //     customerPassword: this.signUpData.password,
    //     customerEmail: this.signUpData.username,
    //     customerId: -1
    //   };
    //   this.loginService.signUpCustomer(customer).subscribe((data: Customer) => {
    //     console.log('Customer signed up:', data);
    //     alert('Sign-up successful! Redirecting to the customer dashboard...');
    //     this.loginService.loggedInUser = { 
    //       username: this.signUpData.username, 
    //       password: this.signUpData.password 
    //     }; // Update the logged-in user
    //     this.router.navigate(['/customer-dashboard']); // Redirect to customer dashboard
    //   });
    // } else {
    //   let vendor: Vendor = {
    //     vendorName: this.signUpData.name,
    //     vendorContact: this.signUpData.contact,
    //     vendorPassword: this.signUpData.password,
    //     vendorEmail: this.signUpData.username,
    //     vendorId: -1
    //   };
    //   this.loginService.signUpVendor(vendor).subscribe((data: Vendor) => {
    //     console.log('Vendor signed up:', data);
    //     alert('Sign-up successful! Redirecting to the vendor dashboard...');
    //     this.loginService.loggedInUser = { 
    //       username: this.signUpData.username, 
    //       password: this.signUpData.password 
    //     }; // Update the logged-in user
    //     localStorage.setItem('vendor', JSON.stringify(data)); // Save vendor details in localStorage
    //     this.router.navigate(['/vendor-dashboard'], {
    //       state: { vendor: data } // Pass vendor data through router state
    //     }); // Redirect to vendor dashboard
    //   });
    // }

    onSignUp() {
      // First, check if the email is already taken
      this.customerService.getAllCustomerEmails().subscribe(customerEmails => {
        this.vendorService.getAllVendorEmails().subscribe(vendorEmails => {
          const allEmails = [...customerEmails, ...vendorEmails];
          
          if (allEmails.includes(this.signUpData.username)) {
            alert('This email is already registered. Please use a different email.');
          } else {
            // Proceed with sign-up
            if (this.signUpData.userType === "Customer") {
              let customer: Customer = {
                customerName: this.signUpData.name,
                customerContact: this.signUpData.contact,
                customerPriority: false,
                customerPassword: this.signUpData.password,
                customerEmail: this.signUpData.username,
                customerId: -1
              };
              this.loginService.signUpCustomer(customer).subscribe((data: Customer) => {
                console.log('Customer signed up:', data);
                alert('Sign-up successful! Redirecting to the customer dashboard...');
                this.loginService.loggedInUser = { 
                  username: this.signUpData.username, 
                  password: this.signUpData.password 
                };
                this.router.navigate(['/customer-dashboard']);
              });
            } else {
              let vendor: Vendor = {
                vendorName: this.signUpData.name,
                vendorContact: this.signUpData.contact,
                vendorPassword: this.signUpData.password,
                vendorEmail: this.signUpData.username,
                vendorId: -1
              };
              this.loginService.signUpVendor(vendor).subscribe((data: Vendor) => {
                console.log('Vendor signed up:', data);
                alert('Sign-up successful! Redirecting to the vendor dashboard...');
                this.loginService.loggedInUser = { 
                  username: this.signUpData.username, 
                  password: this.signUpData.password 
                };
                localStorage.setItem('vendor', JSON.stringify(data));
                this.router.navigate(['/vendor-dashboard'], {
                  state: { vendor: data }
                });
              });
            }
          }
        });
      });
  }

  vendorDashboardDirect(){
    this.router.navigate(['/vendor-dashboard']);
  }

  customerDashboardDirect(){
    this.router.navigate(['/customer-dashboard'])
  }


  confirmAndSignUp(form: NgForm): void {
    if (confirm('Are you sure you want to sign up with the provided details?')) {
      if (form.valid) {
        this.onSignUp();
      } else {
        alert('Please fill in all required fields.');
      }
    } else {
      alert('Sign-up cancelled.');
    }
  }
  
}


