package com.example.wintersport.request;

import jakarta.validation.constraints.NotBlank;

public class CountryRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name.trim();
    }
}
