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

    @GetMapping("{id}")
    public ResponseEntity<CountryResponse> getCountryById(@PathVariable Long id) {
        Optional<Country> country = this.countryRepository.findById(id);
        if (country.isPresent()) {
            CountryResponse countryResponse = new CountryResponse(country.get());
            return ResponseEntity.ok(countryResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CountryRequest countryRequest) {
        Optional<Country> foundCountry = this.countryRepository.findByName(countryRequest.getName());
        if (foundCountry.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Country country = new Country(countryRequest.getName());
        Country savedCountry = this.countryRepository.save(country);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCountry.getId()).toUri();

        return ResponseEntity.created(location).body(new CountryResponse(savedCountry));
    }

    @PutMapping("{id}")
    public ResponseEntity<CountryResponse> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryRequest countryRequest) {
        Optional<Country> country = this.countryRepository.findById(id);
        if (country.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (countryRequest.getName() != null) {
            country.get().setName(countryRequest.getName());
        }

        Country savedCountry = this.countryRepository.save(country.get());
        return ResponseEntity.ok(new CountryResponse(savedCountry));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CountryResponse> deleteCountry(@PathVariable Long id) {
        Optional<Country> country = this.countryRepository.findById(id);
        if (country.isPresent()) {
            try {
                this.countryRepository.deleteById(id);
                return ResponseEntity.ok(new CountryResponse(country.get()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
