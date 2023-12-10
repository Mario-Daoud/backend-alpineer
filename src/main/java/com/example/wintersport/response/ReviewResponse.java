package com.example.wintersport.response;

import com.example.wintersport.domain.Review;

import java.time.LocalDate;

public class ReviewResponse {
    private Long id;
    private int rating;
    private LocalDate date;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.date = review.getDate();
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public LocalDate getDate() {
        return date;
    }
}
