package Controller;

import Service.VendorService;
import SystemParameters.ConfigParameters;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.util.Scanner;

public class VendorController {

    private final HttpClient client = HttpClient.newHttpClient();
    private final MenuController menuController = new MenuController();

    private String username;
    private String id;

    public String getId() {
        return id;
    }

    // Set the username during login
    public void setUsername(String username) {
        this.username = username;
    }

    // Get the stored username
    public String getUsername() {
        return this.username;
    }

    public void getActiveEvents() {
        VendorService service = new VendorService("fetchActive", null, null, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void getAllVendors() {
        // Create a new thread for each service task and start it
        VendorService service = new VendorService("fetchAll", null, null, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getVendorById(int vendorId) {
        VendorService service = new VendorService("fetchById", vendorId, null, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getVendorByEmail(String vendorEmail) {
        // Create the service with the task and vendor email
        VendorService service = new VendorService("fetchByEmail", null, null, vendorEmail);

        // Create a thread to run the service task asynchronously
        Thread thread = new Thread(service);
        thread.start();

        try {
            // Wait for the thread to finish before moving on
            thread.join();

            this.id = service.fetchVendorIdByEmail(vendorEmail); // Fetch vendor id from service

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void saveVendor(String vendorJson) {
        VendorService service = new VendorService("save", null, vendorJson, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteVendor(int vendorId) {
        VendorService service = new VendorService("delete", vendorId, null, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join(); // Waits for this thread to complete before moving on

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean vendorVerification(String username, String password) {
        try {
            // Build the URI with query parameters
            URI uri = new URI("http://localhost:8080/api/vendor/login?username=" + username + "&password=" + password);

            // Create the GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse and return the boolean response
            return Boolean.parseBoolean(response.body());

        } catch (Exception e) {
            return false;
        }
    }

    public void createVendorFromInput() {
        Scanner scanner = new Scanner(System.in);

        // Gather vendor details from user input
        String name = menuController.getValidInput("Enter Vendor Name: ", "string");
        String contact = menuController.getValidInput("Enter Vendor Contact (e.g., phone number): ", "number");
        String email = menuController.getValidInput("Enter Vendor Email: ", "email");
        String password = menuController.notNullChecker("Enter Vendor Password: ", "vendor password");

        // Create JSON string from user inputs
        String vendorJson = String.format(
                "{\"vendorName\": \"%s\", \"vendorContact\": \"%s\", \"vendorEmail\": \"%s\", \"vendorPassword\": \"%s\"}",
                name, contact, email, password);

        VendorService service = new VendorService("save", null, vendorJson, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Vendor creation request has been sent.");
    }
}
