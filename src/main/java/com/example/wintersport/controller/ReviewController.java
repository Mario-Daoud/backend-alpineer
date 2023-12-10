package com.example.wintersport.controller;

import com.example.wintersport.domain.Review;
import com.example.wintersport.exception.ResourceNotFoundException;
import com.example.wintersport.repository.ReviewRepository;
import com.example.wintersport.request.ReviewRequest;
import com.example.wintersport.response.ReviewLocationResponse;
import com.example.wintersport.response.ReviewLocationUserResponse;
import com.example.wintersport.response.ReviewResponse;
import com.example.wintersport.response.ReviewUserResponse;
import com.example.wintersport.service.ReviewService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
@CrossOrigin
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    public ReviewController(ReviewRepository reviewRepository, ReviewService reviewService) {
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewLocationUserResponse>> getAllReviews() {
        List<ReviewLocationUserResponse> reviews = reviewRepository.findAll().stream().map(ReviewLocationUserResponse::new).toList();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("location/{locationId}")
    public ResponseEntity<List<ReviewUserResponse>> getReviewsByLocationId(@PathVariable Long locationId) {
        Optional<List<Review>> reviews = reviewRepository.findByLocationId(locationId);
        if (reviews.isPresent()) {
            List<ReviewUserResponse> reviewResponses = reviews.get().stream().map(ReviewUserResponse::new).toList();
            return ResponseEntity.ok(reviewResponses);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<ReviewLocationResponse>> getReviewsByUserId(@PathVariable Long userId) {
        Optional<List<Review>> reviews = reviewRepository.findByUserId(userId);
        if (reviews.isPresent()) {
            List<ReviewLocationResponse> reviewResponses = reviews.get().stream().map(ReviewLocationResponse::new).toList();
            return ResponseEntity.ok(reviewResponses);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("review"));
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    @PostMapping("{userId}/{locationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewResponse> addReview(@PathVariable Long userId, @PathVariable Long locationId, @Valid @RequestBody ReviewRequest review) {
        try {
            ReviewResponse savedReview = reviewService.addReview(userId, locationId, review);
            return ResponseEntity.ok(savedReview);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest review) {
        try {
            Review updatedReview = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("review"));
            updatedReview.setRating(review.getRating());
            reviewRepository.save(updatedReview);
            return ResponseEntity.ok(new ReviewResponse(updatedReview));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable Long id) {
        try {
            Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("review"));
            reviewRepository.delete(review);
            return ResponseEntity.ok(new ReviewResponse(review));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
