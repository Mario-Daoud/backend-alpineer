package com.example.wintersport.controller;

import com.example.wintersport.domain.Location;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryResponse;
import com.example.wintersport.response.LocationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/location")
@CrossOrigin
public class LocationController {
    private final LocationRepository locationRepository;

    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public ResponseEntity<List<LocationCountryResponse>> getAllLocationsWithCountries() {
        List<Location> locations = this.locationRepository.findAll();
        List<LocationCountryResponse> locationResponses = locations.stream().map(LocationCountryResponse::new).toList();
        return ResponseEntity.ok(locationResponses);
    }

    @GetMapping("featured")
    public ResponseEntity<Set<LocationCountryResponse>> getFeaturedLocation() {
        List<Location> locations = this.locationRepository.findAll();
        Random random = new Random();
        Set<LocationCountryResponse> uniqueFeaturedLocations = new HashSet<>();

        int featuredLocationSize = 3;

        while (uniqueFeaturedLocations.size() < featuredLocationSize) {
            int randomNumber = random.nextInt(locations.size());
            Location randomLocation = locations.get(randomNumber);

            boolean nameExists = uniqueFeaturedLocations.stream()
                    .anyMatch(locationResponse -> locationResponse.getName().equals(randomLocation.getName()));

            if (!nameExists) {
                uniqueFeaturedLocations.add(new LocationCountryResponse(randomLocation));
            }

        }

        return ResponseEntity.ok(uniqueFeaturedLocations);
    }

    @GetMapping("country/{country}")
    public ResponseEntity<List<LocationResponse>> getLocationsByCountry(@PathVariable String country) {
        List<Location> locations = this.locationRepository.findByCountryName(country);
        List<LocationResponse> locationResponses = locations.stream().map(LocationResponse::new).toList();
        return ResponseEntity.ok(locationResponses);
    }
}
