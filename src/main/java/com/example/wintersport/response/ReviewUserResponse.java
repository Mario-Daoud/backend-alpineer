package com.example.wintersport.response;

import com.example.wintersport.domain.Review;

public class ReviewUserResponse extends ReviewResponse {
    private String username;

    public ReviewUserResponse(Review review) {
        super(review);
        this.username = review.getUser().getUsername();
    }

    public String getUsername() {
        return username;
    }
}
