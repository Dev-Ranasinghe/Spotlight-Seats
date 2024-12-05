import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Vendor } from '../model/vendor.model';
import { VendorService } from '../service/vendor.service';
import { LoginService } from '../service/login.service';
import { Event, EventDTO } from '../model/event.model';
import { ConfigService } from '../service/config.service';
import { EventService } from '../service/event.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.css',
  encapsulation: ViewEncapsulation.None, // Disable view encapsulation
})
export class EventCreateComponent implements OnInit {

  constructor(private vendorService: VendorService, private loginService: LoginService, private configService: ConfigService, private eventService: EventService, private router: Router){}

  signInData = {
    userType: '',
    password: '',
    username: ''
  };

  availableTickets = 0;
  systemTotalTickets = 0;
  
  vendor: Vendor = {
    vendorName: '',
    vendorContact: '',
    vendorId: -1,
    vendorPassword: '',
    vendorEmail: ''
  }

  event: EventDTO = {
    eventName: '',
    eventLocation: '',
    totalTickets: 0,
    ticketPrice: 0,
    eventStatus: false,
    vendor: this.vendor
  }

  ngOnInit(): void {
    console.log('Logged-in username:', this.loginService.loggedInUser.username);
    this.vendorService.getVendorByEmail(this.loginService.loggedInUser.username).subscribe((vendor: Vendor) => {
      this.vendor = vendor;
      this.event.vendor = vendor;
      });

      this.configService.getAvailableTickets().subscribe((availableTickets: number) => {
        this.availableTickets = availableTickets;
      });
  }

  validateSeatCapacity():boolean {
    return this.event.totalTickets <= this.availableTickets;
  }

  onSubmit() {
    // Validate the event details
    if (!this.validateEventDetails()) {
      return;
    }
  
    if (!this.validateSeatCapacity()) {
      alert("Event capacity exceeded !!!");
      this.event.totalTickets = 0;
      return;
    }
  
    this.configService.getTotalTickets().subscribe({
      next: (tickets: string) => {
        this.systemTotalTickets = parseInt(tickets, 10) || 0;
        console.log('Total Tickets:', tickets);
  
        this.eventService.createEvent(this.event).subscribe({
          next: (data: Event) => {
            console.log(data);
            alert("Event successfully created :)");
  
            // Parse the total tickets from the system as a number
            const updatedTotalTickets = this.systemTotalTickets + this.event.totalTickets;
  
            // Update the system parameter with the new total
            this.configService.updateSystemParameter("totalTickets", updatedTotalTickets.toString())
              .subscribe({
                next: () => {
                  console.log("Updated Total Tickets:", updatedTotalTickets);
                },
                error: (err) => {
                  console.error('Error updating total tickets:', err);
                }
              });
  
            // Reset the event form
            this.event.eventLocation = '';
            this.event.eventName = '';
            this.event.totalTickets = 0;
  
            this.vendorDashboardDirect();
          },
          error: (err) => {
            console.error('Error creating event:', err);
          }
        });
      },
      error: (err) => {
        console.error('Error fetching total tickets:', err);
      }
    });
  }
  

  // Add a validation method for event details
  validateEventDetails(): boolean {
    if (!this.event.eventName.trim()) {
      alert("Event name is required!");
      return false;
    }
  
    if (!this.event.eventLocation.trim()) {
      alert("Event location is required!");
      return false;
    }
  
    if (this.event.totalTickets <= 0) {
      alert("Total tickets must be a positive number!");
      return false;
    }
  
    if (this.event.ticketPrice <= 0) {
      alert("Ticket price must be a positive number!");
      return false;
    }
  
    return true;
  }
  

  vendorDashboardDirect(){
    this.router.navigate(['/vendor-dashboard']);
  }
}