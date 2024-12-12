import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-simulation',
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css']
})
export class SimulationComponent{

  // Object to hold the form data
  config = {
    totalNoOfTickets: 0,
    maxTicketCapacity: 0,
    ticketReleaseRate: 0,
    customerRetrievalRate: 0
  };

  constructor( private http: HttpClient, private router: Router) {}

  // Method to send the configuration data to the backend and start the simulation
  startSimulation(): void {
    this.http.put('http://localhost:8080/api/simulation/start', this.config).subscribe(
      (response) => {
        console.log('Simulation started:', response);
      },
      (error) => {
        console.error('Error starting simulation:', error);
      }
    );
  }  
  

  // Method to stop the simulation with confirmation and clear the form
  stopSimulation(): void {
    // Confirm before stopping the simulation
    const confirmStop = window.confirm('Are you sure you want to stop the simulation?');
    if (confirmStop) {
      this.http.post('http://localhost:8080/api/simulation/stop', {}).subscribe(() => {
        console.log('Simulation stopped');
        
        // Clear the form after stopping the simulation
        this.clearForm();
      });
    }
  }

  // Method to clear the form fields
  clearForm(): void {
    this.config = {
      totalNoOfTickets: 0,
      maxTicketCapacity: 0,
      ticketReleaseRate: 0,
      customerRetrievalRate: 0
    };
  }

  // Update method for handling form submissions
  updateConfig(): void {
    console.log('Config updated:', this.config);
  }

  adminDashboardDirect(){
    this.router.navigate(['/admin-dashboard'])
  }
}
