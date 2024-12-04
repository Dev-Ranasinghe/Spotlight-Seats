package com.ticketing_system.service;

import com.ticketing_system.entity.*;
import com.ticketing_system.repository.CustomerRepository;
import com.ticketing_system.repository.EventRepository;
import com.ticketing_system.repository.PurchaseRepository;
import com.ticketing_system.repository.TicketPoolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketPoolServiceImpl implements TicketPoolService{

    @Autowired
    private TicketPoolRepository ticketPoolRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private CustomerRepository customerRepository;

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
    public Integer getReleasedTicketCountByEventId(Integer eventId) {
        TicketPool ticketPool = ticketPoolRepository.findByEvent_EventId(eventId);
        return (ticketPool != null) ? ticketPool.getReleasedTicketCount() : null;
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

    @Override
    @Transactional
    public void createPurchase(Integer eventId, Integer customerId, Integer purchaseCount) {
        // Decrement released ticket count
        TicketPool ticketPool = ticketPoolRepository.findByEvent_EventId(eventId);
        if (ticketPool != null) {
            int newCount = ticketPool.getReleasedTicketCount() - purchaseCount;

            if (newCount < 0) {
                throw new IllegalArgumentException("Not enough tickets available.");
            }

            ticketPool.setReleasedTicketCount(newCount);
            ticketPoolRepository.save(ticketPool);
        } else {
            throw new IllegalArgumentException("TicketPool not found for Event ID: " + eventId);
        }

        // Update or create a purchase record
        Optional<Purchase> existingPurchaseOpt = purchaseRepository.findByEvent_EventIdAndCustomer_CustomerId(eventId, customerId);

        if (existingPurchaseOpt.isPresent()) {
            // Increment existing purchase count
            Purchase existingPurchase = existingPurchaseOpt.get();
            existingPurchase.setPurchaseCount(existingPurchase.getPurchaseCount() + purchaseCount);
            purchaseRepository.save(existingPurchase);
        } else {
            // Fetch the Event entity
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found for ID: " + eventId));

            // Fetch the Customer entity
                        Customer customer = customerRepository.findById(customerId)
                                .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + customerId));

            // Set the fetched entities to the Purchase object
                        Purchase newPurchase = new Purchase();
                        newPurchase.setEvent(event);
                        newPurchase.setCustomer(customer);
                        newPurchase.setPurchaseCount(purchaseCount);
                        purchaseRepository.save(newPurchase);

        }
    }

}