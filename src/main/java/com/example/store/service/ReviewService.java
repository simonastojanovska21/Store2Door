package com.example.store.service;

import com.example.store.model.Review;
import com.example.store.model.User;

import java.util.List;

public interface ReviewService {
    List<Review> findAll();
    Review addNewReview(Integer stars, String description, String username);
    List<Review> find3RandomReviews();
}
