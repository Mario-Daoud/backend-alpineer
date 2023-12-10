package com.example.wintersport.response;

import com.example.wintersport.domain.Review;

public class ReviewLocationResponse extends ReviewResponse {
    private String location;

    public ReviewLocationResponse(Review review) {
        super(review);
        this.location = review.getLocation().getName();
    }

    public String getLocation() {
        return location;
    }
}
