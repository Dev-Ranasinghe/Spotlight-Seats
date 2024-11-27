package com.ticketing_system.service;

import com.ticketing_system.entity.Customer;
import com.ticketing_system.entity.Vendor;

import java.util.List;

public interface CustomerService {
    public List<Customer> getAllCustomers();
    public Customer getCustomerById(Integer id);
    public Customer saveCustomer(Customer customer);
    public void deleteCustomer(Integer id);
    public boolean customerVerification(String username, String password);
    public Customer getCustomerByEmail(String email);
}
