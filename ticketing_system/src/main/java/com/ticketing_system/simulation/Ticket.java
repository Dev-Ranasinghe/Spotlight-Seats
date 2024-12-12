package com.ticketing_system.simulation;

import java.math.BigDecimal;

public class Ticket{
    private int id;
    private String event;
    private BigDecimal price;

    public Ticket(int id, String event, BigDecimal price) {
        this.id = id;
        this.event = event;
        this.price = price;
    }

    public String toString() {
        return "Ticket{id=" + id + ", event='" + event + "', price=" + price + '}';
    }

    public int getId() {
        return 0;
    }

    public String getEvent() {
        return "";
    }

    public BigDecimal getPrice() {
        return null;
    }
}