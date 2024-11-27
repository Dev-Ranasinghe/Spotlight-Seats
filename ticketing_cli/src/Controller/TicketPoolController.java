package Controller;

import Service.TicketPoolService;

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
}
