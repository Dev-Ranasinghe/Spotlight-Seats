package Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPoolService implements Runnable {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final HttpClient client = HttpClient.newHttpClient();
    private String task;
    private Integer ticketPoolId;
    private String eventJson;
    private Integer eventId;

    // Constructor to pass task and event-related data
    public TicketPoolService(String task, Integer ticketPoolId, Integer eventId, String eventJson) {
        this.task = task;
        this.eventId = eventId;
        this.eventJson = eventJson;
        this.ticketPoolId = ticketPoolId;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            switch (task) {
                case "fetchAll":
                    fetchAllTicketPools();
                    break;
                case "fetchById":
                    if (eventId != null) {
                        fetchTicketPoolById(eventId);
                    }
                    break;
                case "delete":
                    if (eventId != null) {
                        deleteTicketPool(eventId);
                    }
                    break;
                default:
                    System.out.println("Invalid task specified.");
            }
        } finally {
            lock.unlock();
        }
    }

    // Fetch all events
    public void fetchAllTicketPools() {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/ticket-pool"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("All Ticket Pools: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Fetch event by ID
    public void fetchTicketPoolById(int ticketPoolId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/ticket-pool/" + ticketPoolId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Event by ID " + ticketPoolId + ": " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Delete event
    public void deleteTicketPool(int eventId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/ticket-pool/event/" + eventId))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Deleted Event: " + response.body());
        } catch (Exception e) {
            System.out.println("Exception raised.");
            return;
        } finally {
            lock.unlock();
        }
    }
}
