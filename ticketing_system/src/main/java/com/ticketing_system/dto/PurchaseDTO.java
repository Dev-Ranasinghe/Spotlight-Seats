package com.ticketing_system.dto;

public class PurchaseDTO {
    private Integer purchaseId;
    private Integer purchaseCount;
    private String customerName;
    private String eventName;
    private String eventLocation;
    private Integer ticketPrice;

    public PurchaseDTO(Integer purchaseId, Integer purchaseCount, String customerName, String eventName, String eventLocation, Integer ticketPrice) {
        this.purchaseId = purchaseId;
        this.purchaseCount = purchaseCount;
        this.customerName = customerName;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.ticketPrice = ticketPrice;
    }

    // Getters and Setters
    public Integer getPurchaseId() {
        return purchaseId;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
