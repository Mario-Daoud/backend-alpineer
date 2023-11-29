package com.example.wintersport.controller;

import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.CountryLocationsResponse;
import com.example.wintersport.response.CountryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
}
