package com.ticketing_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data //getters and setters auto generated
@AllArgsConstructor //generate constructor with all the attributes
@NoArgsConstructor //default constructor with no args
@Entity
public class Vendor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Integer vendorId;
    @Column(name = "vendor_name", nullable = false)
    private String vendorName;
    @Column(name = "vendor_contact", nullable = false)
    private String vendorContact;
    @Column(name = "vendor_email", nullable = false)
    private String vendorEmail;
    @Column(name = "vendor_password", nullable = false)
    private String vendorPassword;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude // Prevent circular reference in toString
    private List<Event> events = new ArrayList<>();

    public Vendor(String vendorPassword, String vendorEmail, String vendorName, String vendorContact, Integer vendorId) {
        this.vendorPassword = vendorPassword;
        this.vendorEmail = vendorEmail;
        this.vendorName = vendorName;
        this.vendorContact = vendorContact;
        this.vendorId = vendorId;
    }

    public String getVendorPassword() {
        return vendorPassword;
    }

    public Integer getVendorId() {
        return vendorId;
    }
}