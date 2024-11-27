import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../service/event.service';

@Component({
  selector: 'app-event-management',
  templateUrl: './event-management.component.html',
  styleUrl: './event-management.component.css',
  encapsulation: ViewEncapsulation.None, // Disable view encapsulation
})
export class EventManagementComponent implements OnInit{

  eventId: number | null = null;
  eventName: string = '';
  eventLocation: string = '';
  eventTotalTickets: string = '';
  eventStatus: boolean = false;

    constructor(private route: ActivatedRoute, private eventService: EventService, private router: Router) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
        this.eventId = params['eventId'] ? Number(params['eventId']) : null; // Convert eventId to number if needed
        this.eventName = params['eventName'] || ''; // Default to empty string if null
        this.eventLocation = params['eventLocation'] || '';
        this.eventTotalTickets = params['eventTotalTickets'] || '';
        this.eventStatus = params['eventStatus'] || '';
    });
}

  deleteEvent(eventId: number | null): void {
    if (eventId === null || eventId === undefined) {
      alert('Event ID is not available for deletion.');
      return;
    }

    if (confirm('Are you sure you want to delete this event?')) {
      this.eventService.deleteEvent(eventId).subscribe(
        () => {
          alert('Event deleted successfully!');
          this.vendorDashboardDirect();
        },
        (error) => {
          console.error('Error deleting event:', error);
          alert('Failed to delete the event.');
        }
      );
    }
  }

  vendorDashboardDirect(){
    this.router.navigate(['/vendor-dashboard']);
  }

}
