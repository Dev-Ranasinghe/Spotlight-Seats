
package com.ticketing_system.repository;

import com.ticketing_system.entity.Vendor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository <Vendor, Integer> {

    Vendor findByVendorEmail(String email);

    @Query("SELECT v.vendorEmail FROM Vendor v")
    List<String> findAllVendorEmails();

}