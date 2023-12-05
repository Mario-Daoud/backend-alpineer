package com.example.wintersport.response;

import com.example.wintersport.domain.Country;

public class CountryResponse {
    private Long id;
    private String name;

    public CountryResponse(Country country) {
        this.id = country.getId();
        this.name = country.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
