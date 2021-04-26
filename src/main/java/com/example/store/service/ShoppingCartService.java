package com.example.store.service;

import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCart getActiveShoppingCart(String username);

    ShoppingCart addOrderToShoppingCart(String username, Long productId, int quantity);

    ShoppingCart removeOrderFromShoppingCart(String username, Long orderId);

    void plusQuantity(String username, Long orderId);

    void minusQuantity(String username, Long orderId);

    double calculateTotal(String username);

    ShoppingCart closeActiveShoppingCart(String username);
}
