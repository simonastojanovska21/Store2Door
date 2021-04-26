package com.example.store.service.impl;

import com.example.store.model.Category;
import com.example.store.model.Product;
import com.example.store.model.enums.Measure;
import com.example.store.model.exceptions.ProductNotFound;
import com.example.store.repository.CategoryRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.service.ProductService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> findAllProducts() {
        return this.productRepository.findAll();
    }


    @Override
    public List<Product> findAllProductsWithPriceLessThanAndGreaterThan(Double min, Double max) {
        return this.productRepository.findAllByPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(min,max);
    }

    @Override
    public Product findById(Long code) {
        return this.productRepository.findById(code).orElseThrow(ProductNotFound::new);
    }

    @Override
    public Product addProduct(Long code, String name, Double price, Integer quantity, String description, String imageUrl, Measure measure, List<Long>  categoryId) {
        List<Category> cat=this.categoryRepository.findAllById(categoryId);
        Product product=new Product(code,name,price,(double)0, (double) 0,quantity,description,imageUrl, measure,cat);
        return this.productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long code, String name, Double price, Double discount, Integer quantity, String description, String imageUrl,Measure measure) {
        Product product=this.productRepository.findById(code).orElseThrow(ProductNotFound::new);
        product.setName(name);
        product.setPrice(price);
        Double priceOnDiscount=product.getPrice() - ((product.getPrice()*discount)/100);
        product.setPriceOnDiscount(priceOnDiscount);
        product.setDiscount(discount);
        product.setQuantity(quantity);
        product.setDescription(description);
        product.setImageUrl(imageUrl);
        product.setMeasure(measure);
        return this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long code) {
        Product product=this.productRepository.findById(code).orElseThrow(ProductNotFound::new);
        this.productRepository.delete(product);
    }


    @Override
    public Page<Product> findProducts(Pageable pageable, Optional<Long> categoryId, Optional<String> discount,Optional<String> name,Optional<Double> min,Optional<Double> max) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> products=new ArrayList<>();
        if(categoryId.isPresent() && discount.isPresent() && min.isPresent() && max.isPresent())
        {
            Long id=categoryId.get();
            Category category=this.categoryRepository.findById(id).orElseThrow(ProductNotFound::new);
            Double minimum=min.get();
            Double maximum=max.get();
            products=this.productRepository.findAllByCategoriesContainingAndDiscountGreaterThanAndPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(category,(double)0,minimum,maximum);
        }
        else if(categoryId.isPresent() && discount.isPresent() && discount.get().equals("yes"))
        {
            Long id=categoryId.get();
            Category category=this.categoryRepository.findById(id).orElseThrow(ProductNotFound::new);
            products=this.productRepository.findAllByCategoriesContainingAndDiscountGreaterThan(category,(double)0);
        }
        else if(categoryId.isPresent() && min.isPresent() && max.isPresent())
        {
            Long id=categoryId.get();
            Category category=this.categoryRepository.findById(id).orElseThrow(ProductNotFound::new);
            Double minimum=min.get();
            Double maximum=max.get();
            products=this.productRepository.findAllByCategoriesContainingAndPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(category,minimum,maximum);
        }
        else if(discount.isPresent() && discount.get().equals("yes") && min.isPresent() && max.isPresent())
        {
            Double minimum=min.get();
            Double maximum=max.get();
            products=this.productRepository.findAllByDiscountGreaterThanAndPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual((double)0,minimum,maximum);
        }
        else if (categoryId.isPresent())
        {
            Long id=categoryId.get();
            Category category=this.categoryRepository.findById(id).orElseThrow(ProductNotFound::new);
            products=this.productRepository.findAllByCategoriesContaining(category);
        }
        else if(discount.isPresent() && discount.get().equals("yes"))
        {
            products=this.productRepository.findAllByDiscountGreaterThan((double)0);
        }
        else if(min.isPresent() && max.isPresent())
        {
            Double minimum=min.get();
            Double maximum=max.get();
            products=this.productRepository.findAllByPriceOnDiscountGreaterThanEqualAndPriceOnDiscountLessThanEqual(minimum,maximum);
        }
        else if(name.isPresent())
        {
            String tmp=name.get();
            if(tmp.length()!=0)
                products=this.productRepository.findAllByNameLikeIgnoreCase("%"+tmp+"%");
        }
        else
        {
            products=this.productRepository.findAll();
        }

        List<Product> list;
        if(products.size()<startItem)
        {
            list= Collections.emptyList();
        }
        else
        {
            int toIndex=Math.min(startItem+pageSize,products.size());
            list=products.subList(startItem,toIndex);
        }
        return new PageImpl<Product>(list,PageRequest.of(currentPage,pageSize),products.size());
    }

    @Override
    public List<Product> findProductsOrderedByDiscount() {
        return this.productRepository.findAll(Sort.by(Sort.Direction.DESC,"discount"));
    }

    @Override
    public Product updateDiscount(Long code, Double discount) {
        Product product=this.productRepository.findById(code).orElseThrow(ProductNotFound::new);
        Double priceOnDiscount=product.getPrice() - ((product.getPrice()*discount)/100);
        product.setPriceOnDiscount(Math.round(priceOnDiscount * 100.0) / 100.0);
        product.setDiscount(discount);
        return this.productRepository.save(product);
    }

    @Override
    public List<Product> findProductsOnDiscount() {
        return this.productRepository.findAllByDiscountGreaterThan((double)0);
    }

    @Override
    public Double minPrice() {
        Double min1=this.productRepository.findAll().stream().min(Comparator.comparing(Product::getPrice)).orElseThrow(ProductNotFound::new).getPrice();
        Double min2=this.productRepository.findAll().stream().min(Comparator.comparing(Product::getPriceOnDiscount)).orElseThrow(ProductNotFound::new).getPriceOnDiscount();
        return Math.min(min1,min2);
    }

    @Override
    public Double maxPrice() {
        Double max1=this.productRepository.findAll().stream().max(Comparator.comparing(Product::getPrice)).orElseThrow(ProductNotFound::new).getPrice();
        Double max2=this.productRepository.findAll().stream().max(Comparator.comparing(Product::getPriceOnDiscount)).orElseThrow(ProductNotFound::new).getPriceOnDiscount();
        return Math.max(max1,max2);
    }


}
