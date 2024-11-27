package com.ticketing_system.controller;

import com.ticketing_system.entity.Event;
import com.ticketing_system.service.EventServiceImpl;
import com.ticketing_system.service.TicketPoolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")

public class EventController {

    @Autowired
    private EventServiceImpl eventServiceImpl;
    @Autowired
    private TicketPoolServiceImpl ticketPoolServiceImpl;

    @GetMapping
    public List<Event> getAllEvents(){
        return eventServiceImpl.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Integer id) {
        return eventServiceImpl.getEventById(id);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<Event> getEventsByVendorId(@PathVariable Integer vendorId) {
        return eventServiceImpl.getEventsByVendorId(vendorId);
    }

    @GetMapping("/total-events/active")
    public List<Event> getActiveEvents() {
        return eventServiceImpl.getActiveEvents();
    }

    @GetMapping("/{eventId}/totalTickets")
    public ResponseEntity<Integer> getTotalTicketsByEventId(@PathVariable Integer eventId) {
        try {
            Integer totalTickets = eventServiceImpl.getTotalTicketsByEventId(eventId);
            return ResponseEntity.ok(totalTickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        System.out.println(event);
        return eventServiceImpl.saveEvent(event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        eventServiceImpl.deleteEvent(id);
    }

    @PostMapping("/{eventId}/activate")
    public ResponseEntity<String> activateEvent(@PathVariable Integer eventId) {
        try {
            eventServiceImpl.activateEvent(eventId);
            return ResponseEntity.ok("Event activated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /////

}