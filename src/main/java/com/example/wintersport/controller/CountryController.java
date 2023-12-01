package com.example.wintersport.controller;

import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.CountryLocationsResponse;
import com.example.wintersport.response.CountryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryRepository countryRepository;
    private final LocationRepository locationRepository;

    public CountryController(CountryRepository countryRepository, LocationRepository locationRepository) {
        this.countryRepository = countryRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public List<CountryLocationsResponse> getAllCountries() {
        return this.countryRepository.findAll().stream().map(CountryLocationsResponse::new).collect(Collectors.toList());
    }

    @GetMapping("featured")
    public List<CountryLocationsResponse> getFeaturedCountries() {
        List<CountryLocationsResponse> featuredLocations = new ArrayList<>();

        List<CountryLocationsResponse> allCountries = getAllCountries();

        Random random = new Random();
        while (featuredLocations.size() < 3) {
            int randomNumber = random.nextInt(allCountries.size() - 1);

            if (allCountries.size() <= 3) {
                return allCountries;
            }

            if (!featuredLocations.contains(allCountries.get(randomNumber))) {
                featuredLocations.add(allCountries.get(randomNumber));
            }
        }
        return featuredLocations;
    }
}
