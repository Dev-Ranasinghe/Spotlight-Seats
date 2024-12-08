import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { PurchaseService } from '../service/purchase.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-purchase-history',
  templateUrl: './purchase-history.component.html',
  styleUrl: './purchase-history.component.css',
  encapsulation: ViewEncapsulation.None, // Disable view encapsulation
})
export class PurchaseHistoryComponent implements OnInit{

  customerId: number | null = null;
  purchases: any[] = [];  // Store purchase data

  constructor(private purchaseService: PurchaseService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Subscribe to query parameters to fetch customerId
    this.route.queryParams.subscribe((params) => {
      const id = params['customerId'];
      this.customerId = id ? Number(id) : null; // Parse and validate customerId

      if (this.customerId) {
        this.getPurchasesByCustomerId(); // Call API only if customerId is valid
        console.log("Customer Id: ",this.customerId);
      } else {
        console.error('Invalid or missing customerId');
      }
    });
  }

  // getPurchasesByCustomerId(): void {
  //   if (this.customerId === null) {
  //     console.error('Cannot fetch purchases without a valid customerId');
  //     return;
  //   }

  //   this.purchaseService.getPurchasesByCustomerId(this.customerId).subscribe(
  //     (data) => {
  //       this.purchases = data;
  //       console.log(this.purchases); // Debug log
  //     },
  //     (error) => {
  //       console.error('Error fetching purchases', error);
  //     }
  //   );
  // }

  
  getPurchasesByCustomerId(): void {
    if (this.customerId === null) {
      console.error('Cannot fetch purchases without a valid customerId');
      return;
    }
  
    this.purchaseService.getPurchasesByCustomerId(this.customerId).subscribe(
      (data) => {
        // Transform data to match the old structure if needed
        this.purchases = data.map((purchase: any) => ({
          ...purchase,
          event: {
            eventName: purchase.eventName,
            eventLocation: purchase.eventLocation,
            ticketPrice: purchase.ticketPrice
          }
        }));
        console.log('Fetched purchases:', this.purchases);
      },
      (error) => {
        console.error('Error fetching purchases', error);
      }
    );
  }  

}
