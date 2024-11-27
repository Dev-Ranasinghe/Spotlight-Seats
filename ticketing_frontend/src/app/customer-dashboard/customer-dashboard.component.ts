import { Component, OnInit } from '@angular/core';
import { EventService } from '../service/event.service';
import { Event } from '../model/event.model';
import { CustomerService } from '../service/customer.service';
import { Customer } from '../model/customer.model';
import { LoginService } from '../service/login.service';

@Component({
  selector: 'app-customer-dashboard',
  templateUrl: './customer-dashboard.component.html',
  styleUrl: './customer-dashboard.component.css'
})
export class CustomerDashboardComponent implements OnInit {

  events: Event[] = []; // Declare the property

  customerName: string = '';
  customerEmail: string = '';
  customerContact: string = '';
  customerPriority: boolean = false;
  customerId: number = 0;

  constructor(private eventService: EventService, private customerService: CustomerService, private loginService: LoginService) {}

  ngOnInit(): void {
    this.customerService.getCustomerByEmail(this.loginService.loggedInUser.username).subscribe((customer: Customer) => {
      this.customerName = customer.customerName;
      this.customerEmail = customer.customerEmail;
      this.customerContact = customer.customerContact;
      this.customerPriority = customer.customerPriority;
      this.customerId = customer.customerId;
      this.loadActiveEvents();
      console.log(customer);
      });
  }

  // Load active events from the API
  loadActiveEvents(): void {
    this.eventService.getActiveEvents().subscribe(
      (data: Event[]) => {
        this.events = data;
        console.log('Active Events:', this.events);
      },
      (error) => {
        console.error('Error fetching active events', error);
      }
    );
  }
}