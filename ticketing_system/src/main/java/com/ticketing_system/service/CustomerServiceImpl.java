package com.ticketing_system.service;

import com.ticketing_system.entity.Customer;
import com.ticketing_system.entity.Vendor;
import com.ticketing_system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    // Get a single vendor by email
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByCustomerEmail(email);
    }

    public Customer saveCustomer(Customer customer) {
        // Locking the critical section to avoid race conditions
        lock.lock();
        try {
            // The actual save operation
            return customerRepository.save(customer);
        } finally {
            // Ensuring that the lock is always released after the operation is complete
            lock.unlock();
        }
    }

    public void deleteCustomer(Integer id) {
        lock.lock(); // Acquire the lock
        try {
            customerRepository.deleteById(id);
        } catch (ObjectOptimisticLockingFailureException e) {
            // Handle the exception
            System.out.println("Attempted to delete a stale version of the customer: " + e.getMessage());
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    public boolean customerVerification(String username, String password){
        boolean valid = false;
        Customer customer = customerRepository.findByCustomerEmail(username);
        if(customer == null){
            return false;
        }
        if(customer.getCustomerPassword().equals(password)){
            valid = true;
        }
        return valid;
    }

    public Integer findCustomerIdByVendorEmail(String email) {
        Customer customer = customerRepository.findByCustomerEmail(email);
        return customer.getCustomerId();
    }

    // Inner class to handle Customer operations as threads
    public class CustomerThread extends Thread {
        private final String task;
        private final Integer customerId;
        private final Customer customerData;

        public CustomerThread(String task, Integer customerId, Customer customerData) {
            this.task = task;
            this.customerId = customerId;
            this.customerData = customerData;
        }

        @Override
        public void run() {
            switch (task) {
                case "fetchAll":
                    getAllCustomers();
                    break;
                case "fetchById":
                    getCustomerById(customerId);
                    break;
                case "save":
                    saveCustomer(customerData);
                    break;
                case "delete":
                    deleteCustomer(customerId);
                    break;
                default:
                    System.out.println("Invalid task specified");
            }
        }
    }

    @Override
    public void updateCustomerPriorityToTrue(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Customer with ID " + id + " not found")
        );
        customer.setCustomerPriority(true);
        customerRepository.save(customer);
    }
}