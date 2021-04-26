package com.example.store.service;


import com.example.store.model.Category;
import com.example.store.model.Product;
import com.example.store.model.enums.Measure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();

    List<Product> findAllProductsWithPriceLessThanAndGreaterThan(Double min, Double max);

    Product findById(Long code);

    Product addProduct(Long code, String name, Double price, Integer quantity, String description, String imageUrl, Measure measure, List<Long> categoryId);

    Product updateProduct(Long code, String name, Double price, Double discount, Integer quantity, String description, String imageUrl,Measure measure);

    void deleteProduct(Long code);

    Page<Product> findProducts(Pageable pageable, Optional<Long> categoryId, Optional<String> discount,Optional<String> name,Optional<Double> min, Optional<Double> max);

    List<Product> findProductsOrderedByDiscount();

    Product updateDiscount(Long code, Double discount);

    List<Product> findProductsOnDiscount();

    Double minPrice();

    Double maxPrice();
}
