import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TicketPoolService } from '../service/ticket-pool.service';

@Component({
  selector: 'app-event-analytics',
  templateUrl: './event-analytics.component.html',
  styleUrl: './event-analytics.component.css'
})
export class EventAnalyticsComponent implements OnInit {

  eventId: number | null = null;
  eventName: string = '';
  eventLocation: string = '';
  eventTotalTickets: string = '';
  eventTicketPrice: string = '';
  eventStatus: boolean = false;
  releasedTickets: any = '0';   // Add released tickets property


  constructor(private route: ActivatedRoute, private ticketPoolService: TicketPoolService, private router: Router) {}

  ngOnInit(): void {
    // Extract query parameters
    this.route.queryParams.subscribe(params => {
      this.eventId = params['eventId'] ? Number(params['eventId']) : null;
      this.eventName = params['eventName'] || '';
      this.eventLocation = params['eventLocation'] || '';
      this.eventTotalTickets = params['eventTotalTickets'] || '';
      this.eventTicketPrice = params['eventTicketPrice'] || '';
      this.eventStatus = params['eventStatus'] === 'true'; // Convert string to boolean if needed
    });
    this.fetchReleasedTickets();
    console.log(this.releasedTickets);
  }

  onSubmit(form: any): void {
    console.log('Form Submitted', form.value);
    alert('Form submitted successfully!');
  }

  fetchReleasedTickets(): void {
    if (!this.eventId) {
      console.error('Event ID is not available to fetch released tickets.');
      return;
    }
  
    this.ticketPoolService.getReleasedTicketCount(this.eventId).subscribe({
      next: (count: string) => { 
        const releasedCount = Number(count); // Convert string to number
        if (isNaN(releasedCount)) {
          console.error('Invalid released tickets count received:', count);
          this.releasedTickets = 0; // Fallback for invalid data
        } else {
          this.releasedTickets = releasedCount;
          console.log('Released Tickets:', this.releasedTickets);
        }
      },
      error: (err) => {
        console.error('Error fetching released tickets:', err);
        this.releasedTickets = 0; // Fallback in case of error
      },
      complete: () => {
        console.log('Fetching released tickets completed.');
      }
    });
  }

  calculateSoldTickets(): number {
    const totalTickets = Number(this.eventTotalTickets) || 0; 
    const releasedTickets = Number(this.releasedTickets) 
    return totalTickets - releasedTickets;
  }
  
  calculateExpectedEarning(): number {
    const totalTickets = Number(this.eventTotalTickets) || 0; 
    const ticketPrice = Number(this.eventTicketPrice) || 0;   
    return totalTickets * ticketPrice;
  }
  
  calculateCurrentEarning(): number {
    const totalTickets = Number(this.eventTotalTickets) || 0;
    const releasedTickets = Number(this.releasedTickets) 
    const ticketPrice = Number(this.eventTicketPrice) || 0;
    return (totalTickets - releasedTickets) * ticketPrice;
  }

  calculateToBeEarned(): number {
    const releasedTickets = Number(this.releasedTickets) 
    const ticketPrice = Number(this.eventTicketPrice);
    return releasedTickets * ticketPrice;
  }

  vendorDashboardDirect(){
    this.router.navigate(['/vendor-dashboard']);
  }
}
