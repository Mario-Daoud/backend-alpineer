package com.example.wintersport.repository;

import com.example.wintersport.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SportRepository extends JpaRepository<Sport, Long> {
    Optional<Sport> findByName(String name);
}
