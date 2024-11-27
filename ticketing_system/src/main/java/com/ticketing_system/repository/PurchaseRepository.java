package com.ticketing_system.repository;

import com.ticketing_system.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository <Purchase, Integer> {

}
