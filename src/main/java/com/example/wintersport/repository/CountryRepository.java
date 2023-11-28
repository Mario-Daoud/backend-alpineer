package com.example.wintersport.repository;

import com.example.wintersport.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByName(String name);
    List<Country> findAll();

}
