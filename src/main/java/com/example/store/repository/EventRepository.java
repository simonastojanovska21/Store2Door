package com.example.store.repository;

import com.example.store.model.Event;
import com.example.store.model.ShoppingCart;
import com.example.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    Event findEventByCustomerAndShoppingCart(User customer, ShoppingCart shoppingCart);
}
