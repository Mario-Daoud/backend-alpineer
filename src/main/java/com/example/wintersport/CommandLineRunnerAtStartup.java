package com.example.wintersport;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.domain.Sport;
import com.example.wintersport.domain.User;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.repository.SportRepository;
import com.example.wintersport.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CommandLineRunnerAtStartup implements CommandLineRunner {

    private CountryRepository countryRepository;
    private LocationRepository locationRepository;
    private SportRepository sportRepository;
    private UserRepository userRepository;
    public CommandLineRunnerAtStartup(CountryRepository countryRepository, LocationRepository locationRepository, SportRepository sportRepository, UserRepository userRepository) {
        this.countryRepository = countryRepository;
        this.locationRepository = locationRepository;
        this.sportRepository = sportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Country c1 = new Country();
        c1.setName("country1");
        if (countryRepository.findAll().isEmpty()) {
            countryRepository.save(c1);
        }

        Location l1 = new Location();
        l1.setName("location 1");
        Set<Location> locations = new HashSet<>();
        locations.add(l1);
        if (locationRepository.findAll().isEmpty()) {
            c1.setLocations(locations);
            l1.setCountry(c1);
            locationRepository.save(l1);
        }

        Sport s1 = new Sport();
        s1.setName("sport 1");
        s1.setDescription("description sport 1");
        s1.setDifficulty(3);
        Set<Sport> sports = new HashSet<>();
        sports.add(s1);
        if (sportRepository.findAll().isEmpty()) {
            s1.setLocations(locations);
            l1.setSports(sports);
            sportRepository.save(s1);
        }

        User u1 = new User();
        u1.setUsername("username");

        u1.setPassword("password");
        if (userRepository.findByUsername(u1.getUsername()).isEmpty()) {
            userRepository.save(u1);
        }

    }
}
