package com.example.wintersport.controller;

import com.example.wintersport.domain.Review;
import com.example.wintersport.repository.ReviewRepository;
import com.example.wintersport.response.ReviewResponse;
import com.example.wintersport.response.ReviewUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
@CrossOrigin
public class ReviewController {
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/average/{locationId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long locationId) {
        Double rating = 0.0;
        Optional<List<Review>> reviews = reviewRepository.findByLocationId(locationId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (Review review : reviews.get()) {
            rating += review.getRating();
        }

        rating = rating / reviews.get().size();
        return ResponseEntity.ok(rating);

    }
}
