package com.ticketing_system.repository;

import com.ticketing_system.entity.TicketPool;
import com.ticketing_system.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketPoolRepository extends JpaRepository <TicketPool, Integer> {

    TicketPool findByEvent_EventId(Integer eventId);
    void deleteByEvent_EventId(Integer eventId);

}