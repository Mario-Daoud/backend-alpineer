package com.example.wintersport.response;

import com.example.wintersport.domain.Country;

public class CountryResponse {
    private long id;
    private String name;

    public CountryResponse(Country country) {
        this.id = country.getId();
        this.name = country.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
