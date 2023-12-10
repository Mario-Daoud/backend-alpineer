package com.example.wintersport.controller;

import com.example.wintersport.domain.Review;
import com.example.wintersport.repository.ReviewRepository;
import com.example.wintersport.response.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@CrossOrigin
public class ReviewController {
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("location/{locationId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByLocationId(@PathVariable Long locationId) {
        List<ReviewResponse> reviews = reviewRepository.findByLocationId(locationId).stream().map(ReviewResponse::new).toList();
        return ResponseEntity.ok(reviews);
    }
}
