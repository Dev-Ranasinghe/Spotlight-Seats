package com.ticketing_system.controller;

import com.ticketing_system.entity.Purchase;
import com.ticketing_system.service.PurchaseService;
import com.ticketing_system.service.PurchaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")

public class PurchaseController {

    @Autowired
    private PurchaseServiceImpl purchaseServiceImpl;

    @GetMapping
    public List<Purchase> getAllPurchases(){
        return purchaseServiceImpl.getAllPurchases();
    }

    @GetMapping("/{id}")
    public Purchase getPurchaseById(@PathVariable Integer id) {
        return purchaseServiceImpl.getPurchaseById(id);
    }

    @PostMapping
    public Purchase createPurchase(@RequestBody Purchase purchase) {
        return purchaseServiceImpl.savePurchase(purchase);
    }

}