package com.ticketing_system.repository;

import com.ticketing_system.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Optional<Purchase> findByEvent_EventIdAndCustomer_CustomerId(Integer eventId, Integer customerId);
    List<Purchase> findAllByCustomer_CustomerId(Integer customerId);
    @Query("SELECT SUM(p.purchaseCount) FROM Purchase p WHERE p.customer.customerId = :customerId")
    Integer getTotalPurchaseCountByCustomerId(@Param("customerId") Integer customerId);
}
