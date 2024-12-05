package com.ticketing_system.service;

import com.ticketing_system.entity.Vendor;
import com.ticketing_system.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class VendorServiceImpl implements VendorService{

    // Declare a ReentrantLock to synchronize critical sections
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private VendorRepository vendorRepository;

    // Get all vendors
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // Get a single vendor by id
    public Vendor getVendorById(Integer id) {
        return vendorRepository.findById(id).orElse(null);
    }

    // Get a single vendor by email
    public Vendor getVendorByEmail(String email) {
        return vendorRepository.findByVendorEmail(email);
    }

    // Create or update a vendor (with locking)
    public Vendor saveVendor(Vendor vendor) {
        lock.lock(); // Acquire the lock to avoid race conditions
        try {
            return vendorRepository.save(vendor);
        } finally {
            lock.unlock(); // Always release the lock after the operation
        }
    }

    // Delete a vendor by ID (with locking)
    public void deleteVendor(Integer id) {
        lock.lock(); // Acquire the lock
        try {
            vendorRepository.deleteById(id);
        } finally {
            lock.unlock(); // Always release the lock after the operation
        }
    }

    // Verify vendor credentials
    public boolean vendorVerification(String username, String password){
        boolean valid = false;
        Vendor vendor = vendorRepository.findByVendorEmail(username);
        if(vendor == null){
            return false;
        }
        if(vendor.getVendorPassword().equals(password)){
            valid = true;
        }
        return valid;
    }

    public Integer findVendorIdByVendorEmail(String email) {
        Vendor vendor = vendorRepository.findByVendorEmail(email);
        return vendor.getVendorId();
    }

    // Inner class to handle Vendor operations as threads
    public class VendorThread extends Thread {
        private final String task;
        private final Integer vendorId;
        private final Vendor vendorData;

        public VendorThread(String task, Integer vendorId, Vendor vendorData) {
            this.task = task;
            this.vendorId = vendorId;
            this.vendorData = vendorData;
        }

        @Override
        public void run() {
            switch (task) {
                case "fetchAll":
                    getAllVendors();
                    break;
                case "fetchById":
                    getVendorById(vendorId);
                    break;
                case "save":
                    saveVendor(vendorData);
                    break;
                case "delete":
                    deleteVendor(vendorId);
                    break;
                default:
                    System.out.println("Invalid task specified");
            }
        }
    }

    public List<String> getAllVendorEmails() {
        return vendorRepository.findAllVendorEmails();
    }
}