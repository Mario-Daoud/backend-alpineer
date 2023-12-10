package com.example.wintersport.response;

import com.example.wintersport.domain.Review;

import java.time.LocalDate;

public class ReviewResponse {
    private Long id;
    private int rating;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

}
