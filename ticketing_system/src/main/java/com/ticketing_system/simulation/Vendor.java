package com.ticketing_system.simulation;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Vendor implements Runnable {
    private static final AtomicInteger sharedTicketCounter = new AtomicInteger(0);
    private final int totalTickets;
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

    public Vendor(int totalTickets, TicketPool ticketPool, int ticketReleaseRate) {
        this.totalTickets = totalTickets;
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalTickets; i++) {
            if (!SimulationController.isSimulationRunning()) {
                System.out.println(Thread.currentThread().getName() + ": Simulation stopped, exiting.");
                return; // Exit if simulation is stopped
            }

            int currentTicketId = sharedTicketCounter.incrementAndGet();
            Ticket ticket = new Ticket(currentTicketId, "Event", new BigDecimal("500"));
            ticketPool.addTicket(ticket); // Add the ticket to the shared pool

            try {
                Thread.sleep(ticketReleaseRate * 2000L); // Respect the ticket release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        System.out.println(Thread.currentThread().getName() + ": Finished releasing " + totalTickets + " tickets.");
    }
}
