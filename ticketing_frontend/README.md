# Frontend README

## Project Overview
This project is the frontend of an application designed to handle user sign-ups, customer and vendor dashboards, system configurations, and event management. The frontend is built using Angular and interacts with backend services for data retrieval and updates.

## Features
- **User Sign-up**: Allows customers and vendors to register for the system.
- **Dashboard Navigation**: Provides role-based navigation to customer, vendor, and admin dashboards.
- **System Configuration**: Enables administrators to manage system parameters such as ticket capacities and release rates.
- **Vendor and Customer Management**: Allows administrators to view, add, update, or delete vendor and customer records.
- **Purchase History**: Displays the history of purchases for administrative review.
- **Event Creation**: Facilitates the creation of new events.
- **Validation**: Ensures input data such as email addresses and contact numbers are valid before submission.

## Prerequisites
To run this project locally, ensure the following are installed:
- [Node.js](https://nodejs.org/)
- [Angular CLI](https://angular.io/cli)

## Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd <project-folder>
   ```
3. Install dependencies:
   ```bash
   npm install
   ```

## How to Configure and Start the System
1. **Update Environment Configuration**:
   - Open the `src/environments/environment.ts` file.
   - Update the `apiUrl` property with the correct backend API URL:
     ```typescript
     export const environment = {
       production: false,
       apiUrl: 'http://localhost:8080/api'
     };
     ```

2. **Start the Angular Development Server**:
   ```bash
   ng serve
   ```
   The application will be available at:
   ```
   http://localhost:4200
   ```

3. **Ensure Backend Connectivity**:
   - Confirm that the backend server is running and accessible at the specified `apiUrl`.

## Folder Structure
- **src/app**
  - **components**: Contains all the Angular components (e.g., `system-config`, `sign-up`, `dashboard`, `vendor-management`, `customer-management`, `purchase-history`, `event-create`).
  - **services**: Contains Angular services for API communication (e.g., `config.service.ts`, `vendor.service.ts`, `customer.service.ts`).
  - **models**: Defines TypeScript interfaces for the data models (e.g., `Customer`, `Vendor`).
  - **routes**: Configures application routes for navigation.

## Explanation of UI Controls
### 1. `SignUpComponent`
- Handles user registration.
- Validates email and contact number inputs.
- Supports both customer and vendor registrations.

### 2. `SystemConfigComponent`
- Allows administrators to manage system parameters.
- Validates numeric inputs for system settings.

### 3. `AdminDashboardComponent`
- Provides a central hub for managing vendors, customers, events, and purchase history.

### 4. `VendorDashboardComponent`
- Displays vendor-specific data and tools.

### 5. `CustomerDashboardComponent`
- Displays customer-specific data and tools.

### 6. `PurchaseHistoryComponent`
- Displays historical purchase data for review by administrators.

### 7. `EventCreateComponent`
- Allows administrators to create and manage events.

## API Integration
The application communicates with backend services through REST APIs using the Angular `HttpClientModule`. Ensure the backend is running and accessible before starting the frontend.

## Environment Configuration
Update the `src/environments/environment.ts` file with the correct backend API URLs:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## Validation
The application includes the following validation rules:
- **Email**: Must follow standard email format.
- **Contact Number**: Must be numeric and non-negative.

## Deployment
1. Build the project for production:
   ```bash
   ng build --prod
   ```
2. The compiled files will be available in the `dist/` directory.
3. Deploy the `dist/` folder to your web server.

## Troubleshooting
- If the application does not start, ensure all dependencies are installed correctly and the Angular CLI is up to date.
- Verify that the backend is running and accessible at the specified API URL.

## Contact
For any questions or support, please contact the project maintainer at [sasmith.20230992@iit.ac.lk].