package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.request.CountryRequest;
import com.example.wintersport.response.CountryLocationsResponse;
import com.example.wintersport.response.CountryResponse;
import jakarta.validation.Valid;
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

    @GetMapping("{id}")
    public ResponseEntity<CountryResponse> getCountryById(@PathVariable Long id) {
        return this.countryRepository.findById(id).map(CountryResponse::new).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CountryRequest countryRequest) {
        Optional<Country> foundCountry = this.countryRepository.findByName(countryRequest.getName());
        if (foundCountry.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Country country =  new Country(countryRequest.getName());
        Country savedCountry = this.countryRepository.save(country);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCountry.getId()).toUri();

        return ResponseEntity.created(location).body(new CountryResponse(savedCountry));
    }

    @PutMapping("{id}")
    public CountryResponse updateCountry(@PathVariable Long id, @Valid @RequestBody CountryRequest countryRequest) {
        Country country = this.countryRepository.findById(id).orElseThrow();

        if (countryRequest.getName() != null) {
            country.setName(countryRequest.getName());
        }

        return new CountryResponse(this.countryRepository.save(country));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        Country country = this.countryRepository.findById(id).orElseThrow();
        try {
            this.countryRepository.delete(country);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
