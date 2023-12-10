package com.example.wintersport.response;

import com.example.wintersport.domain.Review;

public class ReviewLocationUserResponse extends ReviewLocationResponse {
    private String user;

    public ReviewLocationUserResponse(Review review) {
        super(review);
        this.user = review.getUser().getUsername();
    }

    public String getUser() {
        return user;
    }
}
