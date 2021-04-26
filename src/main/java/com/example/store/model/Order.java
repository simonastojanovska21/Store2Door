package com.example.store.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    private int quantity;

    public Order(Product product,int quantity)
    {
        this.product=product;
        this.quantity=quantity;
    }
    public double calculateTotal()
    {
        double num=this.product.getPriceOnDiscount()*quantity;
        return Math.round(num * 100.0) / 100.0;
    }
}
