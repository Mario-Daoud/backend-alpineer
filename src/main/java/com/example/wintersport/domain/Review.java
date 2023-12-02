package com.example.wintersport.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Max(5)
    @Min(1)
    private int rating;
    @PastOrPresent
    @NotNull
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Location location;

    public Review() {
    }

    public Review(int rating, LocalDate date, User user, Location location) {
        this.rating = rating;
        this.date = date;
        this.user = user;
        this.location = location;
    }

    public long getId() {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
