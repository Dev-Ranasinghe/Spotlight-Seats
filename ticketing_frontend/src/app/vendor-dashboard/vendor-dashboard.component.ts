import { Component, OnInit } from '@angular/core';
import { VendorService } from '../service/vendor.service';
import { LoginService } from '../service/login.service';
import { Vendor } from '../model/vendor.model';
import { Route, Router } from '@angular/router';
import { EventService } from '../service/event.service';
import { Event } from '../model/event.model';

@Component({
  selector: 'app-vendor-dashboard',
  templateUrl: './vendor-dashboard.component.html',
  styleUrl: './vendor-dashboard.component.css'
})

export class VendorDashboardComponent implements OnInit {

  constructor(private vendorService: VendorService, private loginService: LoginService, private eventService: EventService, private router: Router){}

  vendorName: string = '';
  vendorEmail: string = '';
  vendorId: number = 0;

  events: Event[] = [];

  ngOnInit(): void {
    this.vendorService.getVendorByEmail(this.loginService.loggedInUser.username).subscribe((vendor: Vendor) => {
      this.vendorName = vendor.vendorName;
      this.vendorEmail = vendor.vendorEmail;
      this.vendorId = vendor.vendorId;
      this.loadEvents();
      console.log(vendor);
      });
  }

  loadEvents(): void {
    this.eventService.getEventsByVendorId(this.vendorId).subscribe(
      (data: Event[]) => {
        this.events = data;
        console.log(this.events);
      },
      (error) => {
        console.error('Error fetching events', error);
      }
    );
  }

  eventCreationDirect(){
    this.router.navigate(['/event-create']);
  }

  loginDirect(){
    this.router.navigate(['/login'])
  }

  eventManagementDirect(event: Event): void {
    this.router.navigate(['/event-management'], {
        queryParams: {
            eventId: event.eventId,
            eventName: event.eventName,
            eventLocation: event.eventLocation,
            eventTotalTickets: event.totalTickets,
            eventStatus: event.eventStatus,
            eventTicketPrice: event.ticketPrice,
        }
    });
  }

  deleteEvent(eventId: number): void {
    if (confirm('Are you sure you want to delete this event?')) {
      this.eventService.deleteEvent(eventId).subscribe(
        () => {
          alert('Event deleted successfully!');
          this.loadEvents(); // Refresh the event list
        },
        (error) => {
          console.error('Error deleting event:', error);
          alert('Failed to delete the event.');
        }
      );
    }
  }
  

}
