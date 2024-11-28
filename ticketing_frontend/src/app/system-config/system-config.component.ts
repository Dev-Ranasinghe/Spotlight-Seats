import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ConfigService } from '../service/config.service';

@Component({
  selector: 'app-system-config',
  templateUrl: './system-config.component.html',
  styleUrls: ['./system-config.component.css'], // Corrected styleUrl to styleUrls
  encapsulation: ViewEncapsulation.None, // Disable view encapsulation
})
export class SystemConfigComponent implements OnInit {
  maxTicketCapacity: string = '';
  ticketReleaseRate: string = '';
  customerRetrievalRate: string = '';
  totalTickets: string = '';

  constructor(private configService: ConfigService) {}

  ngOnInit(): void {
    this.loadConfigData();
  }

  // Load initial config data from the service
  loadConfigData(): void {

    this.configService.getMaxCapacity().subscribe((data) => {
      this.maxTicketCapacity = data;
    });

    this.configService.getTotalTickets().subscribe((data) => {
      this.totalTickets = data; 
    });

    this.configService.getTicketReleaseRate().subscribe((data) => {
      this.ticketReleaseRate = data; 
    });

    this.configService.getCustomerRetrievalRate().subscribe((data) => {
      this.customerRetrievalRate = data; 
    });
  }


  updateMaxTicketCapacity(): void {
    console.log('Updated Max Ticket Capacity:', this.maxTicketCapacity); 
      this.configService
        .updateSystemParameter('maxTicketCapacity', this.maxTicketCapacity)
        .subscribe(
          (response) => {
            console.log('Backend response:', response);
            alert('Max Ticket Capacity updated successfully');
          },
          (error) => {
            console.error('Error updating parameter:', error);
            alert('Failed to update Max Ticket Capacity.');
          }
        );
    } 

    updateTicketReleaseRate(): void {
      console.log('Updated Ticket Release Rate:', this.ticketReleaseRate);
        this.configService
          .updateSystemParameter('ticketReleaseRate', this.ticketReleaseRate)
          .subscribe(
            (response) => {
              console.log('Backend response:', response);
              alert('Ticket Release Rate updated successfully');
            },
            (error) => {
              console.error('Error updating parameter:', error);
              alert('Failed to update Ticket Release Rate.');
            }
          );
      } 

      updateCustomerRetrievalRate(): void {
        console.log('Updated Customer Retrieval Rate:', this.customerRetrievalRate); 
          this.configService
            .updateSystemParameter('customerRetrievalRate', this.customerRetrievalRate)
            .subscribe(
              (response) => {
                console.log('Backend response:', response);
                alert('Customer Retrieval Rate updated successfully');
              },
              (error) => {
                console.error('Error updating parameter:', error);
                alert('Failed to update Customer Retrieval Rate.');
              }
            );
        } 
}
