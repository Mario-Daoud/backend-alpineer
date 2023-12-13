package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryResponse;
import com.example.wintersport.response.LocationResponse;
import com.example.wintersport.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/location")
@CrossOrigin
public class LocationController {
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private CountryRepository countryRepository;

    public LocationController(LocationRepository locationRepository,
                              LocationService locationService,
                              CountryRepository countryRepository) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public ResponseEntity<List<LocationCountryResponse>> getAllLocationsWithCountries() {
        List<LocationCountryResponse> locationResponses = locationRepository.findAll()
                .stream()
                .map(LocationCountryResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(locationResponses);
    }

    @GetMapping("/featured")
    public ResponseEntity<Set<LocationCountryResponse>> getFeaturedLocation() {
        return ResponseEntity.ok(locationService.getFeaturedLocations());
    }

    @GetMapping("/country/{countryName}")
    public ResponseEntity<List<LocationCountryResponse>> getLocationsByCountryName(@PathVariable String countryName) {
        return countryRepository.findByName(countryName)
                .map(country -> {
                    List<LocationCountryResponse> locationCountryResponses =
                            locationRepository.findByCountryName(country.getName())
                                    .stream()
                                    .map(LocationCountryResponse::new)
                                    .collect(Collectors.toList());
                    return ResponseEntity.ok(locationCountryResponses);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
