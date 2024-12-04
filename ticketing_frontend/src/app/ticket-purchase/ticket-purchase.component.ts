import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../service/event.service';
import { ConfigService } from '../service/config.service';
import { TicketPoolService } from '../service/ticket-pool.service';

@Component({
  selector: 'app-ticket-purchase',
  templateUrl: './ticket-purchase.component.html',
  styleUrl: './ticket-purchase.component.css',
  encapsulation: ViewEncapsulation.None, // Disable view encapsulation
})

export class TicketPurchaseComponent implements OnInit{

  eventId: number | null = null;
  eventName: string = '';
  eventLocation: string = '';
  eventTotalTickets: string = '';
  eventTicketPrice: string = '';
  eventStatus: boolean = false;
  releasedTicketCount: string = ''; 
  errorMessage: string | null = null;
  ticketCount: number = 0; 
  customerId: number | null = null;
  customerRetrievalRate: number = 0; 
  customerPriority: boolean = false;

    constructor(private route: ActivatedRoute, private eventService: EventService, private router: Router, private configService: ConfigService, private ticketPoolService: TicketPoolService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
        this.eventId = params['eventId'] ? Number(params['eventId']) : null; // Convert eventId to number if needed
        this.eventName = params['eventName'] || ''; // Default to empty string if null
        this.eventLocation = params['eventLocation'] || '';
        this.eventTotalTickets = params['eventTotalTickets'] || '';
        this.eventTicketPrice = params['eventTicketPrice'] || '';
        this.eventStatus = params['eventStatus'] || '';
        this.customerId = params['customerId'] ? Number(params['customerId']) : null; 
        this.customerPriority = params['customerPriority'] === 'true'; // Convert to boolean

        this.loadCustomerRetrievalRate();
        console.log(this.customerRetrievalRate);


        if (this.eventId !== null) {
          this.fetchReleasedTicketCount(this.eventId);
        }
    });
  }


  private fetchReleasedTicketCount(eventId: number): void {
    this.ticketPoolService.getReleasedTicketCount(eventId).subscribe({
      next: (count) => (this.releasedTicketCount = count),
      error: (error) => {
        console.error('Error fetching released ticket count:', error);
        this.errorMessage = 'Unable to retrieve ticket count. Please try again later.';
      }
    });
  }


  loadCustomerRetrievalRate(): void {
    this.configService.getCustomerRetrievalRate().subscribe(
      (response: string) => {
        // Convert the string response to an integer and store it
        this.customerRetrievalRate = parseInt(response, 10);

        // Handle invalid numbers gracefully
        if (isNaN(this.customerRetrievalRate)) {
          console.error('Invalid number received:', response);
          this.customerRetrievalRate = 0; // Default to 0 or any fallback value
        }
        console.log('Customer Retrieval Rate:', this.customerRetrievalRate);
      },
      (error) => {
        console.error('Error fetching customer retrieval rate:', error);
        alert('Failed to fetch customer retrieval rate.'); // Optional: Notify the user
      }
    );
  }


  purchaseTickets(): void {
    if (!this.eventId || this.ticketCount <= 0) {
      alert('Please enter a valid ticket count.');
      return;
    }
  
    if (!this.customerId) {
      alert('Customer ID is missing.');
      return;
    }
  
    // Ensure releasedTicketCount is a valid number
    const availableTickets = parseInt(this.releasedTicketCount, 10);
    if (isNaN(availableTickets)) {
      alert('Unable to verify available tickets. Please try again later.');
      return;
    }
  
    // Ensure customerRetrievalRate is a valid number
    if (!this.customerPriority) {
      if (isNaN(this.customerRetrievalRate)) {
        alert('Your ticket purchase limit is unavailable. Please try again later.');
        return;
      }
    }
  
    // Check if the requested tickets exceed the available tickets
    if (this.ticketCount > availableTickets) {
      alert(
        `You cannot purchase more tickets than the released count. Available tickets: ${availableTickets}.`
      );
      return;
    }
  
    // Enforce limit for non-priority customers
    if (!this.customerPriority && this.ticketCount > this.customerRetrievalRate) {
      alert(
        `As a non-priority customer, you cannot purchase more tickets than your limit. Your limit: ${this.customerRetrievalRate}.`
      );
      return;
    }
  
    const confirmPurchase = confirm(
      `You are about to purchase ${this.ticketCount} tickets for ${this.eventName}. Do you want to proceed?`
    );
  
    if (confirmPurchase) {
      this.ticketPoolService.createPurchase(this.eventId, this.customerId, this.ticketCount).subscribe({
        next: (response: string) => {
          alert(response); // Success message from the backend
          this.ticketCount = 0;
          if (this.eventId !== null) {
            this.fetchReleasedTicketCount(this.eventId); // Optionally refresh ticket count if needed
          }
          this.customerDashboardDirect();
        },
        error: (error) => {
          console.error('Error creating purchase:', error);
          alert('Failed to create purchase. Please try again later.');
        },
      });
    }
  }
  

  customerDashboardDirect(){
    this.router.navigate(['/customer-dashboard']);
  }
}