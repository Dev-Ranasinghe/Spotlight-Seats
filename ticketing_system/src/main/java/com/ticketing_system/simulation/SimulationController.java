package com.ticketing_system.simulation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private static volatile boolean simulationRunning = false;
    private final List<Thread> runningThreads = new ArrayList<>();

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        simulationRunning = false;

        // Join all threads, ensuring they complete their tasks gracefully
        for (Thread thread : runningThreads) {
            try {
                thread.join(500);  // Wait for the thread to finish gracefully
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseEntity.status(500).body("Simulation interrupted while stopping.");
            }
        }
        runningThreads.clear(); // Clear thread list after stopping

        // Return a success message with status 200
        return ResponseEntity.ok("Simulation has been stopped.");
    }

    @PutMapping("/start")
    public ResponseEntity<Object> startSimulation(@RequestBody Configuration Configuration) throws IOException {
        int maxTicketCapacity = Configuration.getMaxTicketCapacity();
        int totalNoOfTickets = Configuration.getTotalNoOfTickets();
        int ticketReleaseRate = Configuration.getTicketReleaseRate();
        int customerRetrievalRate = Configuration.getCustomerRetrievalRate();

        TicketPool ticketPool = new TicketPool(maxTicketCapacity);

        simulationRunning = true;

        // Start vendor threads
        for (int i = 1; i <= 2; i++) {
            Vendor vendor = new Vendor(totalNoOfTickets, ticketPool, ticketReleaseRate);
            Thread vendorThread = new Thread(vendor, "Vendor-" + i);
            runningThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 1; i <= 2; i++) {
            Customer customer = new Customer(ticketPool, 2, customerRetrievalRate);
            Thread customerThread = new Thread(customer, "Customer-" + i);
            runningThreads.add(customerThread);
            customerThread.start();
        }

        // Return a JSON response with a status and the configuration details
        return ResponseEntity.ok(Map.of(
                "message", "Simulation started with the following parameters:",
                "config", Map.of(
                        "Max Ticket Capacity", maxTicketCapacity,
                        "Total No. of Tickets", totalNoOfTickets,
                        "Ticket Release Rate", ticketReleaseRate,
                        "Customer Retrieval Rate", customerRetrievalRate
                )
        ));
    }

    public static boolean isSimulationRunning() {
        return simulationRunning;
    }
}
