package com.ticketing_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data //getters and setters auto generated
@AllArgsConstructor //generate constructor with all the attributes
@NoArgsConstructor //default constructor with no args
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "customer_name", nullable = false)
    private String customerName;
    @Column(name = "customer_contact", nullable = false)
    private String customerContact;
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;
    @Column(name = "customer_priority", nullable = false)
    private boolean customerPriority;
    @Column(name = "customer_password", nullable = false)
    private String customerPassword;

    public String getCustomerPassword() {
        return customerPassword;
    }

    public Integer getCustomerId() {
        return customerId;
    }
}