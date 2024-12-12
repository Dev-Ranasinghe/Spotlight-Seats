import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
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
import { EventAnalyticsComponent } from './event-analytics/event-analytics.component';
import { SimulationComponent } from './simulation/simulation.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    VendorDashboardComponent,
    EventCreateComponent,
    EventManagementComponent,
    CustomerDashboardComponent,
    AdminDashboardComponent,
    VendorManagementComponent,
    CustomerManagementComponent,
    SystemConfigComponent,
    TicketPurchaseComponent,
    PurchaseHistoryComponent,
    EventAnalyticsComponent,
    SimulationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
