package com.example.wintersport.service;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Set<LocationCountryResponse> getFeaturedLocations() {
        List<Location> locations = locationRepository.findAll();
        Random random = new Random();
        Set<LocationCountryResponse> featuredLocations = new HashSet<>();

        int featuredLocationSize = 5;

        while (featuredLocations.size() < featuredLocationSize) {
            int randomNumber = random.nextInt(locations.size());
            Location randomLocation = locations.get(randomNumber);

            boolean isLocationInList = featuredLocations.stream()
                    .anyMatch(locationResponse -> locationResponse.getName().equals(randomLocation.getName()));

            if (!isLocationInList) {
                featuredLocations.add(new LocationCountryResponse(randomLocation));
            }
        }

        return featuredLocations;
    }
}
