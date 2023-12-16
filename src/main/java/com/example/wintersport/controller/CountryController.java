package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.request.CountryRequest;
import com.example.wintersport.response.CountryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin
public class CountryController {
    private final CountryRepository countryRepository;
    private final LocationRepository locationrepository;

    public CountryController(CountryRepository countryRepository, LocationRepository locationrepository) {
        this.countryRepository = countryRepository;
        this.locationrepository = locationrepository;
    }

    @GetMapping
    public List<CountryResponse> getAllCountries() {
        return this.countryRepository.findAll()
                .stream()
                .map(CountryResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getCountryById(@PathVariable Long id) {
        return this.countryRepository.findById(id)
                .map(country -> ResponseEntity.ok(new CountryResponse(country)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CountryRequest countryRequest) {
        Optional<Country> existingCountry = this.countryRepository.findByName(countryRequest.getName());
        if (existingCountry.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Country country = new Country();
        country.setName(countryRequest.getName());

        Country savedCountry = this.countryRepository.save(country);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCountry.getId())
                .toUri();

        return ResponseEntity.created(location).body(new CountryResponse(savedCountry));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> updateCountry(@PathVariable Long id,
                                                         @Valid @RequestBody CountryRequest countryRequest) {
        return this.countryRepository.findById(id)
                .map(country -> {
                    country.setName(countryRequest.getName());
                    Country updatedCountry = this.countryRepository.save(country);
                    return ResponseEntity.ok(new CountryResponse(updatedCountry));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CountryResponse> deleteCountry(@PathVariable Long id) {
        Optional<Country> country = this.countryRepository.findById(id);
        if (country.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<Location> locations = country.get().getLocations();
        this.locationrepository.deleteAll(locations);
        this.countryRepository.delete(country.get());
        return ResponseEntity.ok(new CountryResponse(country.get()));
    }
}
