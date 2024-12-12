package com.ticketing_system.simulation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool{
    private final int maximumTicketCapacity;
    private final List<Ticket> ticketList;

    String logFormat = "%-28s | %-10s | %-16s | %-10s | %-40s | Pool size: %d";

    public TicketPool(int maximumTicketCapacity) {
        this.maximumTicketCapacity = maximumTicketCapacity;
        this.ticketList = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void addTicket(Ticket ticket) {
        while (ticketList.size() >= maximumTicketCapacity) {
            System.out.println(String.format(logFormat,
                    LocalDateTime.now(),
                    Thread.currentThread().getName(),
                    "Exceeded",
                    "Waiting",
                    "N/A",
                    ticketList.size()));
            try {
                wait(); // Wait until space is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e.getMessage());
            }
        }

        // Add the ticket to the pool
        ticketList.add(ticket);
        notifyAll(); // Notify customers that a ticket is available
        System.out.println(String.format(logFormat,
                LocalDateTime.now(),
                Thread.currentThread().getName(),
                "Ticket added",
                "Success",
                ticket,
                ticketList.size()));
    }


    // method for buying tickets from ticket pool
    public synchronized Ticket buyTicket() {
        while (ticketList.isEmpty()) {
            System.out.println(String.format(logFormat,
                    LocalDateTime.now(),
                    Thread.currentThread().getName(),
                    "No tickets",
                    "Waiting",
                    "N/A",
                    ticketList.size()));
            try {
                wait(); // Wait until a ticket is added
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e.getMessage());
            }
        }

        Ticket ticket = ticketList.remove(0); // Remove the first ticket (FIFO)
        notifyAll(); // Notify vendors that space is available
        System.out.println(String.format(logFormat,
                LocalDateTime.now(),
                Thread.currentThread().getName(),
                "Ticket bought",
                "Success",
                ticket,
                ticketList.size()));
        return ticket;
    }

    // method for getting the maximum ticket capacity
    public int getMaximumTicketCapacity() {
        return maximumTicketCapacity;
    }

    // method for getting the current ticket count
    public int getCurrentTicketCount() {
        return ticketList.size();
    }
}