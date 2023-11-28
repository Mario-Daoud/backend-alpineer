package com.example.wintersport;

import com.example.wintersport.domain.Country;
import com.example.wintersport.repository.CountryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerAtStartup implements CommandLineRunner {

    private CountryRepository countryRepository;

    public CommandLineRunnerAtStartup(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (countryRepository.findAll().isEmpty()) {
            Country c1 = new Country();
            c1.setName("country1");
            countryRepository.save(c1);
        }
    }
}
