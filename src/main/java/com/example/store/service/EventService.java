package com.example.store.service;

import com.example.store.model.Event;
import com.example.store.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<Event> findAll();
    Event saveEvent(String start,String username);
    void  deleteEvent(Long id);
    Event getEventForCustomer(String username);
}
