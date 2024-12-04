import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { VendorDashboardComponent } from './vendor-dashboard/vendor-dashboard.component';
import { EventCreateComponent } from './event-create/event-create.component';
import { EventManagementComponent } from './event-management/event-management.component';
import { CustomerDashboardComponent } from './customer-dashboard/customer-dashboard.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { VendorManagementComponent } from './vendor-management/vendor-management.component';
import { CustomerManagementComponent } from './customer-management/customer-management.component';
import { SystemConfigComponent } from './system-config/system-config.component';
import { TicketPurchaseComponent } from './ticket-purchase/ticket-purchase.component';
import { PurchaseHistoryComponent } from './purchase-history/purchase-history.component';

const routes: Routes = [{ path: '', redirectTo: '/login', pathMatch: 'full' }, // Redirect to login by default
                        { path: 'login', component: LoginComponent },
                        { path: 'vendor-dashboard', component: VendorDashboardComponent },
                        { path: 'event-create', component: EventCreateComponent },
                        { path: 'event-management', component: EventManagementComponent},
                        { path: 'admin-dashboard', component: AdminDashboardComponent},
                        { path: 'vendor-management', component: VendorManagementComponent},
                        { path: 'customer-management', component:CustomerManagementComponent},
                        { path: 'system-config', component:SystemConfigComponent},
                        { path: 'ticket-purchase', component:TicketPurchaseComponent},
                        { path: 'purchase-history', component:PurchaseHistoryComponent},
                        { path: 'customer-dashboard', component: CustomerDashboardComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
