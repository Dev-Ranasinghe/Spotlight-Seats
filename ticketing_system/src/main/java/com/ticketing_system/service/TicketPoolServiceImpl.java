package com.ticketing_system.service;

import com.ticketing_system.entity.TicketPool;
import com.ticketing_system.repository.EventRepository;
import com.ticketing_system.repository.TicketPoolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketPoolServiceImpl implements TicketPoolService{

    @Autowired
    private TicketPoolRepository ticketPoolRepository;
    @Autowired
    private EventRepository eventRepository;

    public List<TicketPool> getAllTicketPools() {
        return ticketPoolRepository.findAll();
    }

    @Override
    public TicketPool getTicketPoolById(Integer id) {
        return ticketPoolRepository.findById(id).orElse(null);
    }

    @Override
    public TicketPool saveTicketPool(TicketPool ticketPool) {
        return ticketPoolRepository.save(ticketPool);
    }

    @Override
    public void deleteTicketPool(Integer id) {

    }

    @Override
    @Transactional  // This ensures the delete operation is part of a transaction
    public void deleteTicketPoolByEventId(Integer eventId) {
        // Delete the ticket pool first
        TicketPool ticketPool = ticketPoolRepository.findByEvent_EventId(eventId);
        if (ticketPool != null) {
            ticketPoolRepository.delete(ticketPool);
        }

        // Then delete the associated event
        eventRepository.deleteById(eventId);
    }
}