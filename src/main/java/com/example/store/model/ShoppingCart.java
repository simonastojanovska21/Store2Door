package com.example.store.model;


import com.example.store.model.enums.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Order> orderedProducts;

    public ShoppingCart(User user)
    {
        dateCreated=LocalDateTime.now();
        status=CartStatus.ACTIVE;
        this.user=user;
        orderedProducts=new ArrayList<>();
    }

}
