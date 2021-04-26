package com.example.store.repository;

import com.example.store.model.ShoppingCart;
import com.example.store.model.User;
import com.example.store.model.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    ShoppingCart findByUserAndStatus (User user, CartStatus status);
    ShoppingCart findByUser(User user);
}
