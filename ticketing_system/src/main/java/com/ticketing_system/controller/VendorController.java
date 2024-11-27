
package com.ticketing_system.controller;

import com.ticketing_system.entity.Vendor;
import com.ticketing_system.service.EventServiceImpl;
import com.ticketing_system.service.VendorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    private VendorServiceImpl vendorServiceImpl;
    @Autowired
    private EventServiceImpl eventServiceImpl;

    @GetMapping
    public List<Vendor> getAllVendors(){
        // Create and start a thread to handle fetching all vendors
        VendorServiceImpl.VendorThread fetchAllThread = vendorServiceImpl.new VendorThread("fetchAll", null, null);
        fetchAllThread.start();
        return vendorServiceImpl.getAllVendors();
    }

    @GetMapping("/{id}")
    public Vendor getVendorById(@PathVariable Integer id) {
        // Create and start a thread to handle fetching vendor by id
        VendorServiceImpl.VendorThread fetchByIdThread = vendorServiceImpl.new VendorThread("fetchById", id, null);
        fetchByIdThread.start();
        try{
            return vendorServiceImpl.getVendorById(id);
        }
        finally {
            fetchByIdThread.interrupt();
        }
    }

    @GetMapping("/email/{email}")
    public Vendor getVendorByEmail(@PathVariable String email) {
        return vendorServiceImpl.getVendorByEmail(email);
    }

    @GetMapping("/email-id/{email}")
    public Integer getVendorIdByEmail(@PathVariable String email) {
        return vendorServiceImpl.findVendorIdByVendorEmail(email);
    }

    @PostMapping
    public Vendor createVendor(@RequestBody Vendor vendor) {
        // Create and start a thread to handle saving a new vendor
        VendorServiceImpl.VendorThread saveThread = vendorServiceImpl.new VendorThread("save", null, vendor);
        saveThread.start();
        return vendorServiceImpl.saveVendor(vendor);
    }

    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable Integer id) {
        // Create and start a thread to handle deleting a vendor
        VendorServiceImpl.VendorThread deleteThread = vendorServiceImpl.new VendorThread("delete", id, null);
        deleteThread.start();
        vendorServiceImpl.deleteVendor(id);
    }

    @GetMapping("/login")
    public boolean vendorVerification(@RequestParam String username, @RequestParam String password){
        return vendorServiceImpl.vendorVerification(username, password);
    }

    @GetMapping("/{vendorId}/totalTickets")
    public ResponseEntity<Integer> getTotalTicketsByVendor(@PathVariable Integer vendorId) {
        try {
            Integer totalTickets = eventServiceImpl.getTotalTicketsByVendor(vendorId);
            return ResponseEntity.ok(totalTickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
