package com.example.wintersport.repository;

import com.example.wintersport.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findByLocationId(Long locationId);
    Optional<List<Review>> findByUserId(Long userId);
}
