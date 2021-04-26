package com.example.store.service.impl;

import com.example.store.model.Review;
import com.example.store.model.User;
import com.example.store.model.exceptions.InvalidUsernameException;
import com.example.store.repository.ReviewRepository;
import com.example.store.repository.UserRepository;
import com.example.store.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Review> findAll() {
        return this.reviewRepository.findAll();
    }

    @Override
    public Review addNewReview(Integer stars, String description, String username) {
        User user=this.userRepository.findByUsername(username).orElseThrow(InvalidUsernameException::new);
        Review review=new Review(stars,description,user);
        return this.reviewRepository.save(review);
    }

    @Override
    public List<Review> find3RandomReviews() {
        List<Review> reviews=this.reviewRepository.findAll();
        Collections.shuffle(reviews);
        return reviews.subList(0,3);
    }
}
