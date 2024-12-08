package com.ticketing_system.service;

import com.ticketing_system.dto.PurchaseDTO;
import com.ticketing_system.entity.Purchase;

import java.util.List;

public interface PurchaseService {
    public List<Purchase> getAllPurchases();
    public Purchase getPurchaseById(Integer id);
    public Purchase savePurchase(Purchase purchase);
    public void deletePurchase(Integer id);
    Integer getPurchaseCountByCustomerId(Integer customerId);
//    List<Purchase> getAllPurchasesByCustomerId(Integer customerId);
    // In PurchaseService.java
}
