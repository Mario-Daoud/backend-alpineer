package com.example.wintersport.response;

import com.example.wintersport.domain.Location;

public class LocationResponse {
    private Long id;
    private String name;
    private int snowHeight;
    private String description;
    private int degrees;
    private int trackLength;
    private int chairlifts;

    public LocationResponse(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.snowHeight = location.getSnowHeight();
        this.description = location.getDescription();
        this.degrees = location.getDegrees();
        this.trackLength = location.getTrackLength();
        this.chairlifts = location.getChairlifts();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSnowHeight() {
        return snowHeight;
    }

    public String getDescription() {
        return description;
    }

    public int getDegrees() {
        return degrees;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public int getChairlifts() {
        return chairlifts;
    }
}
