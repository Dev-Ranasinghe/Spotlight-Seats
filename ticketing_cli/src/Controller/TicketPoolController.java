package Controller;

import Service.TicketPoolService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TicketPoolController {
    public void deleteTicketPoolByEventId(int eventId) {
        TicketPoolService service = new TicketPoolService("delete", null, eventId, null);
        Thread thread = new Thread(service);
        thread.start();
        try {
            thread.join();  // Waits for this thread to complete before moving on
        } catch (Exception e) {
            return;
        }
    }

    public String getReleasedTicketCount(Integer eventId) {
        try {
            // Instantiate the service and call the method
            TicketPoolService service = new TicketPoolService("fetchReleasedTicketCount", null, eventId, null);

            return service.fetchReleasedTicketCountByEventId(eventId);

        } catch (Exception e) {
            System.err.println("Error while fetching released ticket count: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
}
