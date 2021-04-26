package com.example.store.service;

import com.example.store.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    List<Category> findAllCategories();
    Category  addSubcategory(Long categoryId, String name);
    Category addCategory(String name);
    Category findById(Long catId);
    List<Category> findSubcategories();
}
