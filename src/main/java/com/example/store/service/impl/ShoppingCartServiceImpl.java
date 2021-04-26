package com.example.store.service.impl;


import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.model.ShoppingCart;
import com.example.store.model.User;
import com.example.store.model.enums.CartStatus;
import com.example.store.model.exceptions.InvalidUsernameException;
import com.example.store.model.exceptions.OrderNotFound;
import com.example.store.model.exceptions.ProductNotFound;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.ShoppingCartRepository;
import com.example.store.repository.UserRepository;
import com.example.store.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.orderRepository=orderRepository;
        this.productRepository = productRepository;
    }


    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        User user=this.userRepository.findByUsername(username).orElseThrow(InvalidUsernameException::new);
        ShoppingCart shoppingCart=this.shoppingCartRepository.findByUserAndStatus(user,CartStatus.ACTIVE);
        if(shoppingCart==null)
        {
            shoppingCart=new ShoppingCart(user);
            this.shoppingCartRepository.save(shoppingCart);
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCart addOrderToShoppingCart(String username, Long productId, int quantity) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        Product product=this.productRepository.findById(productId).orElseThrow(ProductNotFound::new);
        Order order=new Order(product,quantity);
        this.orderRepository.save(order);
        shoppingCart.getOrderedProducts().add(order);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeOrderFromShoppingCart(String username, Long orderId) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        Order order=this.orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        shoppingCart.getOrderedProducts().remove(order);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void plusQuantity(String username, Long orderId) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        Order order=this.orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        shoppingCart.getOrderedProducts().remove(order);
        order.setQuantity(order.getQuantity()+1);
        this.orderRepository.save(order);
        shoppingCart.getOrderedProducts().add(order);
        this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void minusQuantity(String username, Long orderId) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        Order order=this.orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        shoppingCart.getOrderedProducts().remove(order);
        order.setQuantity(order.getQuantity()-1);
        this.orderRepository.save(order);
        shoppingCart.getOrderedProducts().add(order);
        this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public double calculateTotal(String username) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        double total=shoppingCart.getOrderedProducts().stream().mapToDouble(Order::calculateTotal).sum();
        return Math.round(total*100.0)/100.0;
    }

    @Override
    public ShoppingCart closeActiveShoppingCart(String username) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        shoppingCart.setStatus(CartStatus.CLOSED);
        List<Order> orders=shoppingCart.getOrderedProducts();
        for(Order order:orders)
        {
            Product product=order.getProduct();
            Integer quantity=product.getQuantity()-order.getQuantity();
            product.setQuantity(quantity);
            productRepository.save(product);
        }
        return this.shoppingCartRepository.save(shoppingCart);
    }
}
