package com.example.wintersport.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String name;
    @NotNull
    @Column(name = "snow_height")
    private int snowHeight;
    @NotBlank
    private String description;
    @NotNull
    private int degrees;
    @NotNull
    @Min(0)
    private int trackLength;
    @NotNull
    @Min(0)
    private int chairlifts;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    @NotNull
    private Country country;
    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private Set<Sport> sports;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews;

    public Location() {
    }

    public Location(long id, String name, int snowHeight, String description, int degrees, int trackLength, int chairlifts, Country country) {
        this.id = id;
        this.name = name;
        this.snowHeight = snowHeight;
        this.description = description;
        this.degrees = degrees;
        this.trackLength = trackLength;
        this.chairlifts = chairlifts;
        this.country = country;
        this.sports = new HashSet<>();
        this.reviews = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Sport> getSports() {
        return sports;
    }

    public void setSports(Set<Sport> sports) {
        this.sports = sports;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public int getSnowHeight() {
        return snowHeight;
    }

    public void setSnowHeight(int snowHeight) {
        this.snowHeight = snowHeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }

    public int getChairlifts() {
        return chairlifts;
    }

    public void setChairlifts(int chairlifts) {
        this.chairlifts = chairlifts;
    }
}
