package com.example.wintersport.response;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;

public class LocationCountryResponse extends LocationResponse {
    private Country country;

    public LocationCountryResponse(Location location) {
        super(location);
        this.country = location.getCountry();
    }

    public String getCountry() {
        return new CountryResponse(country).getName();
    }
}
