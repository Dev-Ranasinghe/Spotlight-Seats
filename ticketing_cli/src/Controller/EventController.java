package Controller;

import Service.EventService;
import SystemParameters.ConfigParameters;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EventController {

    private MenuController menuController = new MenuController();
    private ConfigParameters configParameters = new ConfigParameters();

    public void getAllEvents() {
        // Create a new thread for each service task and start it
        EventService service = new EventService("fetchAll", null, null, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getActiveEvents() {
        // Use a fixed thread pool for managing threads
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            // Submit the service task to the executor
            EventService service = new EventService("activeEvents", null, null, null);
            Future<?> future = executorService.submit(service);

            // Wait for the task to complete
            future.get(); // Blocks until the task completes or throws an exception
        } catch (Exception e) {
            System.err.println("Error while executing active events task: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the executor service is properly shut down
            executorService.shutdown();
        }
    }

    public void getEventById(int eventId) {
        EventService service = new EventService("fetchById", eventId, null, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getEventsByVendorId(int vendorId) {
        EventService service = new EventService("fetchByVendorId", null, null, vendorId);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(int eventId) {
        EventService service = new EventService("delete", eventId, null, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void activateEvent(int eventId) {
        EventService service = new EventService("activate", eventId, null, null);
        Thread thread = new Thread(() -> service.activateEvent(eventId));
        thread.start();
        try {
            thread.join();  // Wait for the thread to complete
            System.out.println("Event activation request completed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public Integer getTotalTicketsOfEvent(int eventId) {
        EventService service = new EventService("fetchEventTotalTickets", eventId, null, null);

        // Fetch total tickets as a string
        String totalTicketsStr = service.fetchTotalTicketsByEventId(eventId);

        if (totalTicketsStr == null) {
            System.out.println("Failed to fetch total tickets for Event ID: " + eventId);
            return null; // Return null if the response is invalid
        }

        try {
            // Parse and return the integer value
            return Integer.parseInt(totalTicketsStr);
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails
        }
    }


    public void createEventFromInput(String vendorId) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int availableTickets = Integer.parseInt(configParameters.getMaxTickets()) - Integer.parseInt(configParameters.getTotalTickets());
        System.out.println("Only " + availableTickets + " more tickets can be handled by the system.");
        // Gather event details from user input
        String eventName = menuController.getValidInput("Enter Event Name: ", "string");
        String eventLocation = menuController.getValidInput("Enter Event Location: ", "string");
        String totalTickets = menuController.getValidInput("Enter Total Tickets (number): ", "number");
        String ticketPrice = menuController.getValidInput("Enter a single Ticket Price (number): ", "number");
        try {
        if(Integer.parseInt(totalTickets) > availableTickets){
            throw new Exception("Error !!! Only " + availableTickets + " more tickets can be handled by the system.");
        }
        boolean eventStatus = false;

        int totTickets = Integer.parseInt(configParameters.getTotalTickets());
        int updatedTotTickets = totTickets + Integer.parseInt(totalTickets);
        configParameters.updateProperty("totalTickets", String.valueOf(updatedTotTickets));
        // Create JSON string from user inputs
        String eventJson = String.format(
                "{\"eventName\": \"%s\", \"eventLocation\": \"%s\", \"totalTickets\": %s, \"ticketPrice\": %s, \"eventStatus\": %b, \"vendor\": {\"vendorId\": %s}}",
                eventName, eventLocation, totalTickets, ticketPrice, eventStatus, vendorId);

        // Create and start a thread to save the event
        EventService service = new EventService("save", null, eventJson, null);
        Thread thread = new Thread(service);
        thread.start();
            thread.join();  // Waits for this thread to complete before moving on
            System.out.println("Event creation request has been sent.");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
