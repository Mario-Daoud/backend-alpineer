package com.example.wintersport.controller;

import com.example.wintersport.repository.ReviewRepository;
import com.example.wintersport.response.ReviewUserResponse;
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

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ReviewUserResponse>> getReviewsByLocationId(@PathVariable Long locationId) {
        return reviewRepository.findByLocationId(locationId)
                .map(reviews -> ResponseEntity.ok(reviews.stream().map(ReviewUserResponse::new).toList()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
