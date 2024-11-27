package com.ticketing_system.service;

import com.ticketing_system.entity.Vendor;

import java.util.List;

public interface VendorService{
    public List<Vendor> getAllVendors();
    public Vendor getVendorById(Integer id);
    public Vendor getVendorByEmail(String email);
    public Vendor saveVendor(Vendor vendor);
    public void deleteVendor(Integer id);
    public boolean vendorVerification(String username, String password);
//    public Integer findVendorIdByVendorEmail(String email);
}