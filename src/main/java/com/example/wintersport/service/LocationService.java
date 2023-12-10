package com.example.wintersport.service;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.exception.ResourceNotFoundException;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final CountryRepository countryRepository;

    public LocationService(LocationRepository locationRepository, CountryRepository countryRepository) {
        this.locationRepository = locationRepository;
        this.countryRepository = countryRepository;
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

    public List<LocationCountryResponse> getLocationsByCountryId(Long countryId) {
        Country country = this.countryRepository.findById(countryId).orElseThrow(() -> new ResourceNotFoundException("country"));
        List<Location> locations = this.locationRepository.findByCountryName(country.getName());
        List<LocationCountryResponse> locationResponses = locations.stream().map(LocationCountryResponse::new).toList();
        return locationResponses;
    }
}
