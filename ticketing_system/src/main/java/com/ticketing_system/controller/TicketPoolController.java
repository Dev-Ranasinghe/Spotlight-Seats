package com.ticketing_system.controller;

import com.ticketing_system.entity.TicketPool;
import com.ticketing_system.service.TicketPoolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")  // Allow requests only from your frontend URL
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

    @GetMapping("/event/{eventId}/released-tickets")
    public ResponseEntity<Integer> getReleasedTicketCountByEventId(@PathVariable Integer eventId) {
        try {
            Integer count = ticketPoolServiceImpl.getReleasedTicketCountByEventId(eventId);
            if (count != null) {
                return ResponseEntity.ok(count);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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

//    @CrossOrigin(origins = "http://localhost:4200") // Adjust as needed for your Angular app
//    @PatchMapping("/event/{eventId}/decrement")
//    public ResponseEntity<String> decrementReleasedTicketCount(
//            @PathVariable Integer eventId,
//            @RequestParam Integer decrementBy) {
//        try {
//            ticketPoolServiceImpl.decrementReleasedTicketCount(eventId, decrementBy);
//            return ResponseEntity.ok("Decremented released tickets by " + decrementBy + " for Event ID " + eventId);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//        }
//    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/event/{eventId}/purchase")
    public ResponseEntity<String> createPurchase(
            @PathVariable Integer eventId,
            @RequestParam Integer customerId,
            @RequestParam Integer purchaseCount) {
        try {
            ticketPoolServiceImpl.createPurchase(eventId, customerId, purchaseCount);
            return ResponseEntity.ok("Purchase created successfully for Event ID " + eventId + ", Customer ID " + customerId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}