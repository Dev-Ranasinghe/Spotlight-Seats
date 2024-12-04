package com.ticketing_system.repository;

import com.ticketing_system.entity.Event;
import com.ticketing_system.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository <Event, Integer> {

    List<Event> findByVendor_VendorId(Integer vendorId);

    @Query("SELECT e FROM Event e WHERE e.eventStatus = true")
    List<Event> findActiveEvents();
}