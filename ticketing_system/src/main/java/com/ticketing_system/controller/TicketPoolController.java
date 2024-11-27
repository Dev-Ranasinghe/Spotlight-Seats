package com.ticketing_system.controller;

import com.ticketing_system.entity.TicketPool;
import com.ticketing_system.service.TicketPoolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-pool")

public class TicketPoolController {

    @Autowired
    private TicketPoolServiceImpl ticketPoolServiceImpl;

    @GetMapping
    public List<TicketPool> getAllTicketPools(){
        return ticketPoolServiceImpl.getAllTicketPools();
    }

    @GetMapping("/{id}")
    public TicketPool getTicketPoolById(@PathVariable Integer id) {
        return ticketPoolServiceImpl.getTicketPoolById(id);
    }

    @PostMapping
    public TicketPool createTicketPool(@RequestBody TicketPool ticketPool) {
        return ticketPoolServiceImpl.saveTicketPool(ticketPool);
    }

    @DeleteMapping("/{id}")
    public void deleteTicketPool(@PathVariable Integer id) {
        ticketPoolServiceImpl.deleteTicketPool(id);
    }

    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<String> deleteTicketPoolByEventId(@PathVariable Integer eventId) {
        try {
            ticketPoolServiceImpl.deleteTicketPoolByEventId(eventId);
            return ResponseEntity.ok("TicketPool associated with Event ID " + eventId + " has been deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete TicketPool: " + e.getMessage());
        }
    }

}