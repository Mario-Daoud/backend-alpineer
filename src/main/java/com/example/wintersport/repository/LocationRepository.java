package com.example.wintersport.repository;

import com.example.wintersport.domain.Location;
import com.example.wintersport.domain.Review;
import com.example.wintersport.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByName(String name);
}
