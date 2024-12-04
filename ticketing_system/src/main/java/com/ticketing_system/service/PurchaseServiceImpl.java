package com.ticketing_system.service;

import com.ticketing_system.entity.Purchase;
import com.ticketing_system.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Purchase> getAllPurchasesByCustomerId(Integer customerId) {
        return purchaseRepository.findAllByCustomer_CustomerId(customerId);
    }

}