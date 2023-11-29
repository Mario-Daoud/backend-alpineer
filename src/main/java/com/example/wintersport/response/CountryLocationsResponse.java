package com.example.wintersport.response;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;

import java.util.Set;
import java.util.stream.Collectors;

public class CountryLocationsResponse extends CountryResponse {
    private Set<Location> locations;

    public CountryLocationsResponse(Country country) {
        super(country);
        this.locations = country.getLocations();
    }

    public Set<LocationResponse> getLocations() {
        return locations.stream().map(LocationResponse::new).collect(Collectors.toSet());
    }
}
