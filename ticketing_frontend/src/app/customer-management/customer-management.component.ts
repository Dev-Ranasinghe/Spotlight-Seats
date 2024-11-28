import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { CustomerService } from '../service/customer.service';

@Component({
  selector: 'app-customer-management',
  templateUrl: './customer-management.component.html',
  styleUrl: './customer-management.component.css',
  encapsulation: ViewEncapsulation.None // Disable view encapsulation
})
export class CustomerManagementComponent implements OnInit {

  customerId: number | null = null;
  customerName: string = '';
  customerContact: string = '';
  customerEmail: string = '';
  customerPriority: boolean = false;

  constructor(private route: ActivatedRoute, private customerService: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.customerId = params['customerId'] ? Number(params['customerId']) : null; // Convert vendorId to number if needed
      this.customerName = params['customerName'] || ''; // Default to empty string if null
      this.customerContact = params['customerContact'] || '';
      this.customerEmail = params['customerEmail'] || '';
      this.customerPriority = params['customerPriority'] || '';
    });
  }

  deleteCustomer(customerId: number | null): void {
    if (customerId === null || customerId === undefined) {
      alert('Customer ID is not available for deletion.');
      return;
    }
  
    if (confirm('Are you sure you want to delete this customer?')) {
      console.log('Deleting customer with ID:', customerId);
  
      this.customerService.deleteCustomer(customerId).subscribe(
        () => {
          console.log(customerId, 'customer deleted successfully.');
          this.adminDashboardDirect(); // Navigate to the admin dashboard after deletion
        },
        (error) => {
          console.error('Error deleting customer:', error);
          alert('Failed to customer the vendor. Please try again later.');
        }
      );
    }
  }

  adminDashboardDirect(){
    this.router.navigate(['/admin-dashboard']);
  }
}
