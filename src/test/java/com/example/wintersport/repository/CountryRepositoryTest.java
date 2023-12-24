package com.example.wintersport.repository;

import com.example.wintersport.domain.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CountryRepositoryTest {
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void findAllCountriesExisting() {
        countryRepository.save(new Country("Netherlands"));
        countryRepository.save(new Country("Germany"));
        countryRepository.save(new Country("France"));

        assertThat(countryRepository.findAll()).hasSize(3);

        assertThat(countryRepository.findAll().get(0).getName()).isEqualTo("Netherlands");
        assertThat(countryRepository.findAll().get(1).getName()).isEqualTo("Germany");
        assertThat(countryRepository.findAll().get(2).getName()).isEqualTo("France");
    }

    @Test
    public void findAllNotExisting() {
        assertThat(countryRepository.findAll()).isEmpty();
    }

    @Test
    public void findByNameExisting() {
        countryRepository.save(new Country("Netherlands"));

        assertThat(countryRepository.findByName("Netherlands").get().getName()).isEqualTo("Netherlands");
    }

    @Test
    public void findByNameNotExisting() {
        assertThat(countryRepository.findByName("Netherlands")).isEmpty();
    }

    @Test
    public void findByNameEmpty() {
        assertThat(countryRepository.findByName("")).isEmpty();
    }


    @Test
    public void findByNameBlank() {
        assertThat(countryRepository.findByName(" ")).isEmpty();
    }
}
