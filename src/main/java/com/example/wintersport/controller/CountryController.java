package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.request.CountryRequest;
import com.example.wintersport.response.CountryLocationsResponse;
import com.example.wintersport.response.CountryResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
}
