import { Component, OnInit } from '@angular/core';
import { EventService } from '../service/event.service';
import { Event } from '../model/event.model';
import { CustomerService } from '../service/customer.service';
import { Customer } from '../model/customer.model';
import { LoginService } from '../service/login.service';
import { Router } from '@angular/router';
import { PurchaseService } from '../service/purchase.service';

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
  customerVip: string = '';

  constructor(private eventService: EventService, private customerService: CustomerService, private loginService: LoginService, private router: Router, private purchaseService: PurchaseService) {}

  ngOnInit(): void {
    // Load Customer Details
    const storedCustomer = localStorage.getItem('customer');
    if (storedCustomer) {
      const customer = JSON.parse(storedCustomer);
      this.customerName = customer.customerName;
      this.customerEmail = customer.customerEmail;
      this.customerContact = customer.customerContact;
      this.customerPriority = customer.customerPriority;
      this.customerId = customer.customerId;
      this.loadActiveEvents();
      this.customerVipCheck();
      this.checkAndUpdateCustomerPriority(); // Check and update priority


    } else {
      this.customerService.getCustomerByEmail(this.loginService.loggedInUser.username)
        .subscribe((customer: Customer) => {
          this.customerName = customer.customerName;
          this.customerEmail = customer.customerEmail;
          this.customerContact = customer.customerContact;
          this.customerPriority = customer.customerPriority;
          this.customerId = customer.customerId;
          localStorage.setItem('customer', JSON.stringify(customer)); // Save customer data
          this.loadActiveEvents();
          this.customerVipCheck();
          this.checkAndUpdateCustomerPriority(); // Check and update priority
        });
    }
  }
  
  customerVipCheck(): void {
    try {
      if (typeof this.customerPriority === 'boolean') {
        // Correct the logic to match the intended behavior
        this.customerVip = this.customerPriority ? 'VIP CUSTOMER' : 'REGULAR CUSTOMER';
      } else {
        throw new Error('Invalid customerPriority value');
      }
    } catch (error) {
      console.error('Error determining customer VIP status:', error);
      this.customerVip = 'UNKNOWN STATUS'; // Set a default value
    }
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

  ticketPurchaseDirect(event: Event): void {
    this.router.navigate(['/ticket-purchase'], {
        queryParams: {
            eventId: event.eventId,
            eventName: event.eventName,
            eventLocation: event.eventLocation,
            eventTotalTickets: event.totalTickets,
            eventStatus: event.eventStatus,
            eventTicketPrice: event.ticketPrice,
            customerId: this.customerId,
            customerPriority: this.customerPriority,
        }
    });
  }

  purchaseHistoryDirect(): void {
    this.router.navigate(['/purchase-history'], {
        queryParams: {
            customerId: this.customerId,
        }
    });
  }

  // New Method: Check and update customer priority
  checkAndUpdateCustomerPriority(): void {
    this.purchaseService.getPurchaseCountByCustomerId(this.customerId).subscribe(
      (purchaseCount: number) => {
        console.log('Purchase Count:', purchaseCount);
        if (purchaseCount >= 50 && !this.customerPriority) { // Update only if not already a priority customer
          this.customerService.updateCustomerPriorityToTrue(this.customerId).subscribe(() => {
            this.customerPriority = true;
            this.customerVipCheck(); // Update VIP status
            console.log('Customer priority updated to true.');
          });
        }
      },
      (error) => {
        console.error('Error fetching purchase count', error);
      }
    );
  }
}