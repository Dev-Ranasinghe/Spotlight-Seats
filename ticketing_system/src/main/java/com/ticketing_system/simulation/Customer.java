package com.ticketing_system.simulation;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketsPerRetrieval;
    private final int customerRetrievalRate;

    public Customer(TicketPool ticketPool, int ticketsPerRetrieval, int customerRetrievalRate) {
        this.ticketPool = ticketPool;
        this.ticketsPerRetrieval = ticketsPerRetrieval;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        while (SimulationController.isSimulationRunning()) {  // Check if the simulation is running
            for (int i = 0; i < ticketsPerRetrieval; i++) {
                Ticket ticket = ticketPool.buyTicket();

                try {
                    // Wait for the customer retrieval rate in seconds after each purchase
                    Thread.sleep(customerRetrievalRate * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getName() + ": Interrupted, exiting.");
                    return; // Exit the thread safely
                }
            }

            try {
                // Wait a bit longer before starting the next purchase cycle
                Thread.sleep(customerRetrievalRate * 2000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + ": Interrupted, exiting.");
                return; // Exit the thread safely
            }
        }

        System.out.println(Thread.currentThread().getName() + ": Simulation stopped, exiting.");
    }
}
