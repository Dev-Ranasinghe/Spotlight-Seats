package com.ticketing_system.repository;

import com.ticketing_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Integer> {

    Customer findByCustomerEmail(String email);

    @Query("SELECT c.customerEmail FROM Customer c")
    List<String> findAllCustomerEmails();
}
