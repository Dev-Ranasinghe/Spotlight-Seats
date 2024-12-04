import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { VendorService } from '../service/vendor.service';
import { ConfigService } from '../service/config.service';

@Component({
  selector: 'app-vendor-management',
  templateUrl: './vendor-management.component.html',
  styleUrls: ['./vendor-management.component.css'],
  encapsulation: ViewEncapsulation.None, // Disable view encapsulation
})
export class VendorManagementComponent implements OnInit {

  vendorId: number | null = null;
  vendorName: string = '';
  vendorContact: string = '';
  vendorEmail: string = '';

  constructor(private route: ActivatedRoute, private vendorService: VendorService, private router: Router, private configService: ConfigService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.vendorId = params['vendorId'] ? Number(params['vendorId']) : null; // Convert vendorId to number if needed
      this.vendorName = params['vendorName'] || ''; // Default to empty string if null
      this.vendorContact = params['vendorContact'] || '';
      this.vendorEmail = params['vendorEmail'] || '';
    });
  }

  // deleteVendor(vendorId: number | null): void {
  //   if (vendorId === null || vendorId === undefined) {
  //     alert('Vendor ID is not available for deletion.');
  //     return;
  //   }
  
  //   if (confirm('Are you sure you want to delete this vendor?')) {
  //     console.log('Deleting vendor with ID:', vendorId);
  
  //     this.vendorService.deleteVendor(vendorId).subscribe(
  //       () => {
  //         console.log(vendorId, 'vendor deleted successfully.');
  //         this.adminDashboardDirect(); // Navigate to the admin dashboard after deletion
  //       },
  //       (error) => {
  //         console.error('Error deleting vendor:', error);
  //         alert('Failed to delete the vendor. Please try again later.');
  //       }
  //     );
  //   }
  // }

  deleteVendor(vendorId: number | null): void {
    if (vendorId === null || vendorId === undefined) {
      alert('Vendor ID is not available for deletion.');
      return;
    }
  
    if (confirm('Are you sure you want to delete this vendor?')) {
      console.log('Fetching total tickets for vendor with ID:', vendorId);
  
      // Fetch the vendor's total tickets
      this.vendorService.getTotalTicketsByVendor(vendorId).subscribe({
        next: (vendorTotalTickets: number) => {
          console.log('Vendor Total Tickets:', vendorTotalTickets);
  
          // Update the system's total tickets
          this.configService.getTotalTickets().subscribe({
            next: (systemTotalTickets: string) => {
              const updatedTotalTickets = parseInt(systemTotalTickets, 10) - vendorTotalTickets;
              console.log('Updated Total Tickets:', updatedTotalTickets);
  
              this.configService.updateSystemParameter('totalTickets', updatedTotalTickets.toString())
                .subscribe({
                  next: () => {
                    console.log('System total tickets updated successfully.');
  
                    // Delete the vendor
                    this.vendorService.deleteVendor(vendorId).subscribe({
                      next: () => {
                        console.log(vendorId, 'vendor deleted successfully.');
                        alert('Vendor deleted successfully.');
                        this.adminDashboardDirect(); // Navigate to the admin dashboard after deletion
                      },
                      error: (err) => {
                        console.error('Error deleting vendor:', err);
                        alert('Failed to delete the vendor. Please try again later.');
                      }
                    });
                  },
                  error: (err) => {
                    console.error('Error updating total tickets:', err);
                    alert('Failed to update total tickets. Vendor deletion aborted.');
                  }
                });
            },
            error: (err) => {
              console.error('Error fetching system total tickets:', err);
              alert('Failed to fetch system total tickets. Vendor deletion aborted.');
            }
          });
        },
        error: (err) => {
          console.error('Error fetching vendor total tickets:', err);
          alert('Failed to fetch vendor total tickets. Vendor deletion aborted.');
        }
      });
    }
  }
  

  adminDashboardDirect(){
    this.router.navigate(['/admin-dashboard']);
  }
}