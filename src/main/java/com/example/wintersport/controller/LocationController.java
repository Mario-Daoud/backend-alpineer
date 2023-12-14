package com.example.wintersport.controller;

import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin
public class LocationController {
    private final LocationRepository locationRepository;
    private CountryRepository countryRepository;

    public LocationController(LocationRepository locationRepository,
                              CountryRepository countryRepository) {
        this.locationRepository = locationRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public ResponseEntity<List<LocationCountryReviewResponse>> getAllLocations() {
        List<LocationCountryReviewResponse> locationResponses = locationRepository.findAll()
                .stream()
                .map(LocationCountryReviewResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(locationResponses);
    }

    @GetMapping("/country/{countryName}")
    public ResponseEntity<List<LocationCountryReviewResponse>> getLocationsByCountryName(@PathVariable String countryName) {
        return countryRepository.findByName(countryName)
                .map(country -> {
                    List<LocationCountryReviewResponse> locationCountryResponses =
                            locationRepository.findByCountryName(country.getName())
                                    .stream()
                                    .map(LocationCountryReviewResponse::new)
                                    .collect(Collectors.toList());
                    return ResponseEntity.ok(locationCountryResponses);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
