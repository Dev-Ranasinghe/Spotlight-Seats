import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { VendorService } from '../service/vendor.service';

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

  constructor(private route: ActivatedRoute, private vendorService: VendorService, private router: Router) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.vendorId = params['vendorId'] ? Number(params['vendorId']) : null; // Convert vendorId to number if needed
      this.vendorName = params['vendorName'] || ''; // Default to empty string if null
      this.vendorContact = params['vendorContact'] || '';
      this.vendorEmail = params['vendorEmail'] || '';
    });
  }

  deleteVendor(vendorId: number | null): void {
    if (vendorId === null || vendorId === undefined) {
      alert('Vendor ID is not available for deletion.');
      return;
    }
  
    if (confirm('Are you sure you want to delete this vendor?')) {
      console.log('Deleting vendor with ID:', vendorId);
  
      this.vendorService.deleteVendor(vendorId).subscribe(
        () => {
          console.log(vendorId, 'vendor deleted successfully.');
          this.adminDashboardDirect(); // Navigate to the admin dashboard after deletion
        },
        (error) => {
          console.error('Error deleting vendor:', error);
          alert('Failed to delete the vendor. Please try again later.');
        }
      );
    }
  }

  adminDashboardDirect(){
    this.router.navigate(['/admin-dashboard']);
  }
}