package com.example.wintersport.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Max(5)
    @Min(1)
    private int rating;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Location location;

    public Review() {
    }

    public Review(int rating, User user, Location location) {
        this.rating = rating;
        this.user = user;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
