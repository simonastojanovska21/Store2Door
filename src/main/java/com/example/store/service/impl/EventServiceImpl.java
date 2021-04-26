package com.example.store.service.impl;


import com.example.store.model.Event;
import com.example.store.model.ShoppingCart;
import com.example.store.model.User;
import com.example.store.model.enums.CartStatus;
import com.example.store.model.exceptions.EventNotFound;
import com.example.store.model.exceptions.InvalidUsernameException;
import com.example.store.repository.EventRepository;
import com.example.store.repository.ShoppingCartRepository;
import com.example.store.repository.UserRepository;
import com.example.store.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, ShoppingCartRepository shoppingCartRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public Event saveEvent(String start,String username) {
        LocalDateTime time=LocalDateTime.parse(start);
        User user=this.userRepository.findByUsername(username).orElseThrow(InvalidUsernameException::new);
        String description="Delivery for address "+user.getAddress();
        ShoppingCart shoppingCart=this.shoppingCartRepository.findByUserAndStatus(user, CartStatus.ACTIVE);
        if(shoppingCart==null)
        {
            shoppingCart=new ShoppingCart(user);
            this.shoppingCartRepository.save(shoppingCart);
        }
        Event checkEvent=this.eventRepository.findEventByCustomerAndShoppingCart(user,shoppingCart);
        if(checkEvent!=null)
            this.deleteEvent(checkEvent.getId());
        Event event=new Event("Delivery",description,time,time.plusHours(1),user,shoppingCart);
        return this.eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        Event event=this.eventRepository.findById(id).orElseThrow(EventNotFound::new);
        this.eventRepository.delete(event);
    }

    @Override
    public Event getEventForCustomer(String username) {
        User user=this.userRepository.findByUsername(username).orElseThrow(InvalidUsernameException::new);
        ShoppingCart shoppingCart=this.shoppingCartRepository.findByUserAndStatus(user,CartStatus.ACTIVE);
        return this.eventRepository.findEventByCustomerAndShoppingCart(user,shoppingCart);
    }

}
