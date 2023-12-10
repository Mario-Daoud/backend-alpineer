package com.example.wintersport.service;

import com.example.wintersport.domain.Location;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Set<LocationCountryResponse> fillFeaturedLocations() {
        List<Location> locations = this.locationRepository.findAll();
        Random random = new Random();
        Set<LocationCountryResponse> uniqueFeaturedLocations = new HashSet<>();

        int featuredLocationSize = 5;

        while (uniqueFeaturedLocations.size() < featuredLocationSize) {
            int randomNumber = random.nextInt(locations.size());
            Location randomLocation = locations.get(randomNumber);

            boolean nameExists = uniqueFeaturedLocations.stream()
                    .anyMatch(locationResponse -> locationResponse.getName().equals(randomLocation.getName()));

            if (!nameExists) {
                uniqueFeaturedLocations.add(new LocationCountryResponse(randomLocation));
            }

        }

        return uniqueFeaturedLocations;

    }


}
