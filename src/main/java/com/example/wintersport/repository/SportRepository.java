package com.example.wintersport.repository;

import com.example.wintersport.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportRepository extends JpaRepository<Sport, Long> {
    List<Sport> findAll();
}
