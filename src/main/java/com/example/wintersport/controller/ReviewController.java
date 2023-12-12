package com.example.wintersport.controller;

import com.example.wintersport.repository.ReviewRepository;
import com.example.wintersport.request.ReviewRequest;
import com.example.wintersport.response.ReviewLocationResponse;
import com.example.wintersport.response.ReviewLocationUserResponse;
import com.example.wintersport.response.ReviewResponse;
import com.example.wintersport.response.ReviewUserResponse;
import com.example.wintersport.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
        List<ReviewLocationUserResponse> reviews = reviewRepository.findAll()
                .stream()
                .map(ReviewLocationUserResponse::new)
                .toList();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ReviewUserResponse>> getReviewsByLocationId(@PathVariable Long locationId) {
        return reviewRepository.findByLocationId(locationId)
                .map(reviews -> ResponseEntity.ok(reviews.stream().map(ReviewUserResponse::new).toList()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewLocationResponse>> getReviewsByUserId(@PathVariable Long userId) {
        return reviewRepository.findByUserId(userId)
                .map(reviews -> ResponseEntity.ok(reviews.stream().map(ReviewLocationResponse::new).toList()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewLocationUserResponse> getReviewById(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .map(review -> ResponseEntity.ok(new ReviewLocationUserResponse(review)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/{locationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewResponse> addReview(@PathVariable Long userId,
                                                    @PathVariable Long locationId,
                                                    @Valid @RequestBody ReviewRequest review) {
        try {
            ReviewResponse savedReview = reviewService.addReview(userId, locationId, review);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedReview.getId())
                    .toUri();

            return ResponseEntity.created(location).body(savedReview);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest updatedReview) {
        return reviewRepository.findById(id)
                .map(existingReview -> {
                    existingReview.setRating(updatedReview.getRating());
                    reviewRepository.save(existingReview);
                    return ResponseEntity.ok(new ReviewResponse(existingReview));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .map(review -> {
                    reviewRepository.delete(review);
                    return ResponseEntity.ok(new ReviewResponse(review));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
