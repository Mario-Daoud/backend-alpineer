package com.example.wintersport.response;

import com.example.wintersport.domain.Location;

public class LocationResponse {
    private long id;
    private String name;

    public LocationResponse(Location location) {
        this.id = location.getId();
        this.name = location.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
