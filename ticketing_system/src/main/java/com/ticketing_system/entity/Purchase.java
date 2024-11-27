package com.ticketing_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data //getters and setters auto generated
@AllArgsConstructor //generate constructor with all the attributes
@NoArgsConstructor //default constructor with no args
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Integer purchaseId;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false)
    private Event event;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private Customer customer;
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
}
