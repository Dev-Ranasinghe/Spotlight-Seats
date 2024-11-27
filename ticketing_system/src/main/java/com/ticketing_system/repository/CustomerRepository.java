package com.ticketing_system.repository;

import com.ticketing_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Integer> {

    Customer findByCustomerEmail(String email);

}
