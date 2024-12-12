import { Component, OnInit } from '@angular/core';
import { VendorService } from '../service/vendor.service';
import { Vendor } from '../model/vendor.model';
import { CustomerService } from '../service/customer.service';
import { Customer } from '../model/customer.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit{

  vendors: Vendor[] = []; // Declare the vendor array
  customers: Customer[] = []; // Declare the customer array


  constructor(private vendorService: VendorService, private customerService: CustomerService, private router: Router){}

  ngOnInit(): void {
    this.loadAllVendors();
    this.loadAllCustomers();
  }

  // Load active events from the API
  loadAllVendors(): void {
    this.vendorService.getAllVendors().subscribe(
      (data: Vendor[]) => {
        this.vendors = data;
        console.log('Vendors:', this.vendors);
      },
      (error) => {
        console.error('Error fetching vendors', error);
      }
    );
  }

  // Load active events from the API
  loadAllCustomers(): void {
    this.customerService.getAllCustomers().subscribe(
      (data: Customer[]) => {
        this.customers = data;
        console.log('Customers:', this.customers);
      },
      (error) => {
        console.error('Error fetching customers', error);
      }
    );
  }

  vendorManagementDirect(vendor: Vendor): void {
    this.router.navigate(['/vendor-management'], {
      queryParams: {
        vendorName: vendor.vendorName,
        vendorContact: vendor.vendorContact,
        vendorEmail: vendor.vendorEmail,
        vendorId: vendor.vendorId
      }
    });
  }  

  customerManagementDirect(customer: Customer){
    this.router.navigate(['/customer-management'], {
      queryParams: {
        customerName: customer.customerName,
        customerContact: customer.customerContact,
        customerEmail: customer.customerEmail,
        customerId: customer.customerId,
        customerPriority: customer.customerPriority
      }
    });
  }
  
  systemConfigDirect(){
    this.router.navigate(['/system-config']);
  }

  simulationDirect(){
    this.router.navigate(['/simulation']);
  }

  loginDirect(){
    this.router.navigate(['/login'])
  }
}