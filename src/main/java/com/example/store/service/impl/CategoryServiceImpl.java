package com.example.store.service.impl;

import com.example.store.model.Category;
import com.example.store.model.exceptions.CategoryNotFound;
import com.example.store.repository.CategoryRepository;
import com.example.store.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllCategories() {
        return this.categoryRepository.findDistinctBySubCategoriesNotNull();
    }

    @Override
    public Category addSubcategory(Long categoryId, String name) {
        Category category=this.categoryRepository.findById(categoryId).orElseThrow(CategoryNotFound::new);
        Category subCategory=new Category(name);
        category.getSubCategories().add(subCategory);
        this.categoryRepository.save(subCategory);
        this.categoryRepository.save(category);
        return subCategory;
    }

    @Override
    public Category addCategory(String name) {
        Category category=new Category(name);
        return this.categoryRepository.save(category);
    }

    @Override
    public Category findById(Long catId) {
        return this.categoryRepository.findById(catId).orElseThrow(CategoryNotFound::new);
    }

    @Override
    public List<Category> findSubcategories() {
        return this.categoryRepository.findDistinctBySubCategoriesNull();
    }

}