package com.example.wintersport.controller;

import com.example.wintersport.domain.Location;
import com.example.wintersport.exception.ResourceNotFoundException;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryResponse;
import com.example.wintersport.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/location")
@CrossOrigin
public class LocationController {
    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public LocationController(LocationRepository locationRepository, LocationService locationService) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationCountryResponse>> getAllLocationsWithCountries() {
        List<Location> locations = this.locationRepository.findAll();
        List<LocationCountryResponse> locationResponses = locations.stream().map(LocationCountryResponse::new).toList();
        return ResponseEntity.ok(locationResponses);
    }

    @GetMapping("featured")
    public ResponseEntity<Set<LocationCountryResponse>> getFeaturedLocation() {
        Set<LocationCountryResponse> featuredLocations = this.locationService.fillFeaturedLocations();
        return ResponseEntity.ok(featuredLocations);

    }

    @GetMapping("country/{countryId}")
    public ResponseEntity<List<LocationCountryResponse>> getLocationsByCountryId(@PathVariable Long countryId) {
        try {
            List<LocationCountryResponse> locationResponses = this.locationService.getLocationsByCountryId(countryId);
            return ResponseEntity.ok(locationResponses);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
