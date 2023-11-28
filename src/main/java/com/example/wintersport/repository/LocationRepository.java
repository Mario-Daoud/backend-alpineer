package com.example.wintersport.repository;

import com.example.wintersport.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAll();
    Optional<Location> findByName(String locationName);
    Optional<List<Location>> findAllByCountry(String countryName);
}
