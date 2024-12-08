package com.ticketing_system.service;

import com.ticketing_system.dto.PurchaseDTO;
import com.ticketing_system.entity.Purchase;
import com.ticketing_system.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private PurchaseRepository purchaseRepository;

    // Get all purchases
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    // Get a single purchase
    public Purchase getPurchaseById(Integer id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    // Create or update a purchase
    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    // Delete a purchase by ID
    public void deletePurchase(Integer id) {
        purchaseRepository.deleteById(id);
    }

    @Override
    public Integer getPurchaseCountByCustomerId(Integer customerId) {
        return purchaseRepository.getTotalPurchaseCountByCustomerId(customerId);
    }

//    @Override
//    public List<Purchase> getAllPurchasesByCustomerId(Integer customerId) {
//        return purchaseRepository.findAllByCustomer_CustomerId(customerId);
//    }

    public List<PurchaseDTO> getAllPurchasesByCustomerId(Integer customerId) {
        List<Purchase> purchases = purchaseRepository.findAllByCustomer_CustomerId(customerId);
        // Convert each Purchase to PurchaseDTO
        return purchases.stream()
                .map(purchase -> new PurchaseDTO(
                        purchase.getPurchaseId(),
                        purchase.getPurchaseCount(),
                        purchase.getCustomer().getCustomerName(),   // Assuming Customer has a getCustomerName() method
                        purchase.getEvent().getEventName(),
                        purchase.getEvent().getEventLocation(),
                        purchase.getEvent().getTicketPrice()
                ))
                .collect(Collectors.toList());
    }
}