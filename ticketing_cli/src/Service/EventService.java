package Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.locks.ReentrantLock;

public class EventService implements Runnable {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final HttpClient client = HttpClient.newHttpClient();
    private String task;
    private Integer eventId;
    private String eventJson;
    private Integer vendorId;

    // Constructor to pass task and event-related data
    public EventService(String task, Integer eventId, String eventJson, Integer vendorId) {
        this.task = task;
        this.eventId = eventId;
        this.eventJson = eventJson;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            switch (task) {
                case "fetchAll":
                    fetchAllEvents();
                    break;
                case "fetchById":
                    if (eventId != null) {
                        fetchEventById(eventId);
                    }
                    break;
                case "fetchByVendorId":
                    if (vendorId != null) {
                        fetchEventsByVendorId(vendorId);
                    }
                    break;
                case "save":
                    if (eventJson != null) {
                        saveEvent(eventJson);
                    }
                    break;
                case "delete":
                    if (eventId != null) {
                        deleteEvent(eventId);
                    }
                case "fetchEventTotalTickets":
                    if (eventId != null) {
                        fetchTotalTicketsByEventId(eventId);
                    }
                    break;
                case "activate":
                    if (eventId != null) {
                        activateEvent(eventId);
                    }
                    break;
                case "activeEvents":
                    fetchActiveEvents();
                    break;
                default:
                    System.out.println("Invalid task specified.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void fetchActiveEvents() {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event/total-events/active"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body().trim();

            if (responseBody.isEmpty() || responseBody.equals("[]")) {
                System.out.println("No active events found.");
                return;
            }

            // Remove the outer brackets [ ] and split the JSON objects by "},{" (assuming uniform formatting)
            String[] events = responseBody.substring(1, responseBody.length() - 1).split("\\},\\{");

            // Print the headers
            System.out.println(String.format("%-10s %-30s %-20s %-15s %-15s %-15s",
                    "Event ID", "Event Name", "Location", "Total Tickets", "Ticket Price", "Status"));
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            for (String event : events) {
                // Clean up each event string by removing any curly braces left
                event = event.replace("{", "").replace("}", "");

                // Split key-value pairs by commas
                String[] keyValuePairs = event.split(",");

                // Initialize variables to store extracted values
                String eventId = "", eventName = "", eventLocation = "", totalTickets = "", ticketPrice = "", eventStatus = "";

                // Iterate over each key-value pair and extract values based on keys
                for (String pair : keyValuePairs) {
                    String[] keyValue = pair.split(":");

                    // Remove any surrounding quotes from keys and values
                    String key = keyValue[0].trim().replaceAll("\"", "");
                    String value = keyValue[1].trim().replaceAll("\"", "");

                    switch (key) {
                        case "eventId":
                            eventId = value;
                            break;
                        case "eventName":
                            eventName = value;
                            break;
                        case "eventLocation":
                            eventLocation = value;
                            break;
                        case "totalTickets":
                            totalTickets = value;
                            break;
                        case "ticketPrice":
                            ticketPrice = value;
                            break;
                        case "eventStatus":
                            eventStatus = value.equals("true") ? "Active" : "Inactive";
                            break;
                    }
                }

                // Print each row in a formatted table format
                System.out.println(String.format("%-10s %-30s %-20s %-15s %-15s %-15s",
                        eventId, eventName, eventLocation, totalTickets, ticketPrice, eventStatus));
            }
        } catch (Exception e) {
            System.err.println("Error fetching active events: " + e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }



    public String fetchTotalTicketsByEventId(int eventId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event/" + eventId + "/totalTickets"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String eventTotalTickets = response.body().trim();

            if (eventTotalTickets.isEmpty()) {
                System.out.println("API response is empty for Event ID: " + eventId);
                return null; // Return null if the response is empty
            }

            System.out.println("Total Tickets API Response: " + eventTotalTickets);
            return eventTotalTickets;
        } catch (Exception e) {
            return null; // Return null if an exception occurs
        } finally {
            lock.unlock();
        }
    }

    // Activate event
    public void activateEvent(int eventId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event/" + eventId + "/activate"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Event Activation Response: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    // Fetch all events
    public void fetchAllEvents() {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("All Events: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Fetch event by ID
    public void fetchEventById(int eventId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event/" + eventId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Event by ID " + eventId + ": " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void fetchEventsByVendorId(int vendorId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event/vendor/" + vendorId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();

            System.out.println();
            // Remove the outer brackets [ ] and split the JSON objects by "},{" (assuming uniform formatting)
            String[] events = responseBody.substring(1, responseBody.length() - 1).split("\\},\\{");

            // Print the headers
            System.out.println(String.format("%-10s %-30s %-20s %-15s %-15s %-15s", "Event ID", "Event Name", "Location", "Total Tickets", "Ticket Price", "Status"));
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            for (String event : events) {
                // Clean up each event string by removing any curly braces left
                event = event.replace("{", "").replace("}", "");

                // Split key-value pairs by commas
                String[] keyValuePairs = event.split(",");

                // Initialize variables to store extracted values
                String eventId = "", eventName = "", eventLocation = "", totalTickets = "", ticketPrice = "", eventStatus = "";

                // Iterate over each key-value pair and extract values based on keys
                for (String pair : keyValuePairs) {
                    String[] keyValue = pair.split(":");

                    // Remove any surrounding quotes from keys and values
                    String key = keyValue[0].trim().replaceAll("\"", "");
                    String value = keyValue[1].trim().replaceAll("\"", "");

                    switch (key) {
                        case "eventId":
                            eventId = value;
                            break;
                        case "eventName":
                            eventName = value;
                            break;
                        case "eventLocation":
                            eventLocation = value;
                            break;
                        case "totalTickets":
                            totalTickets = value;
                            break;
                        case "ticketPrice":
                            ticketPrice = value;
                            break;
                        case "eventStatus":
                            eventStatus = value.equals("true") ? "Active" : "Inactive";
                            break;
                    }
                }

                // Print each row in a formatted table format
                System.out.println(String.format("%-10s %-30s %-20s %-15s %-15s %-15s",
                        eventId, eventName, eventLocation, totalTickets, ticketPrice, eventStatus));
            }
        } catch (Exception e) {
            return;
        } finally {
            lock.unlock();
        }
    }

    // Save event
    public void saveEvent(String eventJson) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(eventJson))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Saved Event: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Delete event
    public void deleteEvent(int eventId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event/" + eventId))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Deleted Event: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
