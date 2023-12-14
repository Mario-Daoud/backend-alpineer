package com.example.wintersport.response;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.domain.Review;

import java.util.List;


public class LocationCountryReviewResponse extends LocationResponse {
    private Country country;
    private Double averageRating;

    public LocationCountryReviewResponse(Location location) {
        super(location);
        this.country = location.getCountry();
        this.averageRating = calculateAverageRating(location.getReviews());
    }

    public Double calculateAverageRating(List<Review> reviews) {
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public String getCountry() {
        return new CountryResponse(country).getName();
    }

    public Double getAverageRating() {
        return averageRating;
    }
}
