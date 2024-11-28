package com.ticketing_system.service;

import com.ticketing_system.entity.Event;
import com.ticketing_system.entity.TicketPool;
import com.ticketing_system.entity.Vendor;
import com.ticketing_system.repository.EventRepository;
import com.ticketing_system.repository.TicketPoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EventServiceImpl implements EventService{

    private final Lock lock = new ReentrantLock();
    // Map to manage event-specific locks
    private final ConcurrentHashMap<Integer, Lock> eventLocks = new ConcurrentHashMap<>();

    @Autowired
    private EventRepository eventRepository;

    // Get all customers
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get a single event
    public Event getEventById(Integer id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Event> task = () -> {
            return eventRepository.findById(id).orElse(null);
        };
        Future<Event> future = executor.submit(task);
        Event event = null;
        try {
            event = future.get();  // This will block and wait for the result
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        return event;
    }

    // Delete an event by ID
    public void deleteEvent(Integer id) {
        Thread thread = null;
        try {
            thread = new Thread(() -> {
                 lock.lock();
                 try {
                     eventRepository.deleteById(id);
                 } finally {
                     lock.unlock();
                 }
             });
            thread.start();
        } finally{
            thread.interrupt();
        }
    }

    // Get events by vendor ID
    public List<Event> getEventsByVendorId(Integer vendorId) {
        return eventRepository.findByVendor_VendorId(vendorId);
    }

    @Autowired
    private TicketPoolRepository ticketPoolRepository;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // Ensure new events are created with `eventStatus` = false by default
    public Event saveEvent(Event event) {
        if (event.getEventId() == null) { // New event
            event.setEventStatus(false);
        }
        return eventRepository.save(event);
    }

    // Activate an event and release tickets
//    public void activateEvent(Integer eventId) {
//        Event event = eventRepository.findById(eventId).orElse(null);
//        if (event == null) {
//            throw new IllegalArgumentException("Event not found");
//        }
//
//        if (event.isEventStatus()) {
//            throw new IllegalStateException("Event is already active");
//        }
//
//        event.setEventStatus(true);
//        eventRepository.save(event);
//
//        // Start releasing tickets to the ticket pool
//        executorService.submit(() -> releaseTickets(event));
//    }

    // Activate an event and release tickets
    public void activateEvent(Integer eventId) {
        Lock lock = eventLocks.computeIfAbsent(eventId, id -> new ReentrantLock());
        lock.lock();
        try {
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                throw new IllegalArgumentException("Event not found");
            }

            if (event.isEventStatus()) {
                throw new IllegalStateException("Event is already active");
            }

            event.setEventStatus(true);
            eventRepository.save(event);

            // Start releasing tickets to the ticket pool
            executorService.submit(() -> releaseTickets(event));
        } finally {
            lock.unlock();
            eventLocks.remove(eventId); // Clean up lock to prevent memory leaks
        }
    }

    private void releaseTickets(Event event) {
        try {
            // Fetch existing TicketPool for the event
            TicketPool ticketPool = ticketPoolRepository.findByEvent_EventId(event.getEventId());

            // If no TicketPool exists, create a new one
            if (ticketPool == null) {
                ticketPool = new TicketPool();
                ticketPool.setEvent(event);
                ticketPool.setReleasedTicketCount(0); // Initialize with 0 tickets
            }

            int ticketsReleased = ticketPool.getReleasedTicketCount();

            while (ticketsReleased < event.getTotalTickets()) {
                int releaseCount = Math.min(10, event.getTotalTickets() - ticketsReleased);

                // Update the released ticket count
                ticketPool.setReleasedTicketCount(ticketsReleased + releaseCount);

                // Save the updated record to the database
                ticketPoolRepository.save(ticketPool);

                ticketsReleased += releaseCount;

                // Sleep for 5 seconds
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ticket release interrupted", e);
        }
    }

    @Override
    public Integer getTotalTicketsByEventId(Integer eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        return event.getTotalTickets();
    }

    public Integer getTotalTicketsByVendor(Integer vendorId) {
        List<Event> events = eventRepository.findByVendor_VendorId(vendorId);
        return events.stream()
                .mapToInt(Event::getTotalTickets)
                .sum();
    }

    public boolean isEventActive(Integer eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
        return event.isEventStatus(); // Use the `isEventStatus` field to determine if active
    }


    public List<Event> getActiveEvents() {
        return eventRepository.findActiveEvents();
    }
}