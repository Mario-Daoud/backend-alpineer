package com.example.wintersport.repository;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void findByNameExisting() {
        Country france = countryRepository.save(new Country("France"));
        Location alps = locationRepository.save(new Location("Alps", 100, "Alps description", 2, 100, 10, france));

        assertThat(locationRepository.findByName("Alps").get().getId()).isEqualTo(alps.getId());
    }

    @Test
    public void findByNameNonExisting() {
        assertThat(locationRepository.findByName("Alps")).isEmpty();
    }

    @Test
    public void findByNameEmpty() {
        assertThat(locationRepository.findByName("")).isEmpty();
    }

    @Test
    public void findByNameNull() {
        assertThat(locationRepository.findByName(null)).isEmpty();
    }

    @Test
    public void findByNameBlank() {
        assertThat(locationRepository.findByName(" ")).isEmpty();
    }

    @Test
    public void findByCountryNameExisting() {
        Country france = countryRepository.save(new Country("France"));
        Location alps = locationRepository.save(new Location("Alps", 100, "Alps description", 2, 100, 10, france));

        assertThat(locationRepository.findByCountryName("France").get(0).getId()).isEqualTo(alps.getId());
    }

    @Test
    public void findByCountryNameNonExisting() {
        assertThat(locationRepository.findByCountryName("France")).isEmpty();
    }

    @Test
    public void findByCountryNameEmpty() {
        assertThat(locationRepository.findByCountryName("")).isEmpty();
    }

    @Test
    public void findByCountryNameNull() {
        assertThat(locationRepository.findByCountryName(null)).isEmpty();
    }

    @Test
    public void findByCountryNameBlank() {
        assertThat(locationRepository.findByCountryName(" ")).isEmpty();
    }

}
