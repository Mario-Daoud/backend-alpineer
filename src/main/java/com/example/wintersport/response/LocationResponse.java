package com.example.wintersport.response;

import com.example.wintersport.domain.Location;

public class LocationResponse {
    private long id;
    private String name;
    private int snowHeight;
    private String description;

    public LocationResponse(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.snowHeight = location.getSnowHeight();
        this.description = location.getDescription();
    }

    public long getId() {
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
}
