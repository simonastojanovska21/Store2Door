package com.example.store.model;

import com.example.store.model.enums.Measure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    private Long code;

    private String name;

    private Double price;

    private Double priceOnDiscount;

    private Double discount;

    private Integer quantity;

    @Column(length = 10000)
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Measure measure;

    @ManyToMany
    private List<Category> categories;
}
