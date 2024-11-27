package com.ticketing_system.service;

import com.ticketing_system.entity.Event;

import java.util.List;

public interface EventService {
    public List<Event> getAllEvents();
    public Event getEventById(Integer id);
    public Event saveEvent(Event event);
    public void deleteEvent(Integer id);
    public List<Event> getEventsByVendorId(Integer vendorId);
    public void activateEvent(Integer eventId);
    public Integer getTotalTicketsByEventId(Integer eventId);
}