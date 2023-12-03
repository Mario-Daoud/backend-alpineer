package com.example.wintersport.controller;

import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.CountryLocationsResponse;
import com.example.wintersport.response.CountryResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/country")
@CrossOrigin
public class CountryController {
    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public List<CountryResponse> getAllCountries() {
        return this.countryRepository.findAll().stream().map(CountryResponse::new).collect(Collectors.toList());
    }

    @GetMapping("location")
    public List<CountryResponse> getAllCountriesWithLocations() {
        return this.countryRepository.findAll().stream().map(CountryLocationsResponse::new).collect(Collectors.toList());
    }
}
