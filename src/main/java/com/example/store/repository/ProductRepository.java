package com.example.store.repository;


import com.example.store.model.Category;
import com.example.store.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByCategoriesContaining(Category category);

    List<Product> findAllByDiscountGreaterThan(Double num);

    List<Product> findAllByPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(Double min, Double max);

    List<Product> findAllByCategoriesContainingAndDiscountGreaterThan(Category category,Double num);

    List<Product> findAllByCategoriesContainingAndPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(Category category,Double min,Double max);

    List<Product> findAllByDiscountGreaterThanAndPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(Double num,Double min, Double max);

    List<Product> findAllByCategoriesContainingAndDiscountGreaterThanAndPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(Category category,Double num,Double min, Double max);

    List<Product> findAllByNameLikeIgnoreCase(String name);
}
