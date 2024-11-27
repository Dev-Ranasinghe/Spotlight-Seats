package com.ticketing_system.controller;

import com.ticketing_system.entity.Customer;
import com.ticketing_system.entity.Vendor;
import com.ticketing_system.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @GetMapping
    public List<Customer> getAllCustomers() {
        CustomerServiceImpl.CustomerThread fetchAllThread = customerServiceImpl.new CustomerThread("fetchAll", null, null);
        fetchAllThread.start();
        return customerServiceImpl.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Integer id) {
        CustomerServiceImpl.CustomerThread fetchByIdThread = customerServiceImpl.new CustomerThread("fetchById", id, null);
        fetchByIdThread.start();
        return customerServiceImpl.getCustomerById(id);
    }

    @GetMapping("/email/{email}")
    public Customer getCustomerByEmail(@PathVariable String email) {
        return customerServiceImpl.getCustomerByEmail(email);
    }

    @GetMapping("/email-id/{email}")
    public Integer getCustomerIdByEmail(@PathVariable String email) {
        return customerServiceImpl.findCustomerIdByVendorEmail(email);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        CustomerServiceImpl.CustomerThread saveThread = customerServiceImpl.new CustomerThread("save", null, customer);
        saveThread.start();
        return customerServiceImpl.saveCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        CustomerServiceImpl.CustomerThread deleteThread = customerServiceImpl.new CustomerThread("delete", id, null);
        deleteThread.start();
        customerServiceImpl.deleteCustomer(id);
    }

    @GetMapping("/login")
    public boolean customerVerification(@RequestParam String username, @RequestParam String password) {
        return customerServiceImpl.customerVerification(username, password);
    }
}