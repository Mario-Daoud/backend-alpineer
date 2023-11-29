package com.example.wintersport;

import com.example.wintersport.domain.*;
import com.example.wintersport.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class CommandLineRunnerAtStartup implements CommandLineRunner {

    private CountryRepository countryRepository;
    private LocationRepository locationRepository;
    private SportRepository sportRepository;
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;

    public CommandLineRunnerAtStartup(CountryRepository countryRepository,
                                      LocationRepository locationRepository,
                                      SportRepository sportRepository,
                                      UserRepository userRepository,
                                      ReviewRepository reviewRepository) {
        this.countryRepository = countryRepository;
        this.locationRepository = locationRepository;
        this.sportRepository = sportRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
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
        l1.setCountry(c1);
        l1.setSnowHeight(122);
        l1.setDescription("perfect for beginners");
        Set<Location> locations = new HashSet<>();
        locations.add(l1);
        if (locationRepository.findAll().isEmpty()) {
            locationRepository.save(l1);
        }

        Sport s1 = new Sport();
        s1.setName("sport 1");
        s1.setDescription("description sport 1");
        s1.setDifficulty(3);
        Set<Sport> sports = new HashSet<>();
        sports.add(s1);
        s1.setLocations(locations);
        l1.setSports(sports);
        if (sportRepository.findAll().isEmpty()) {
            locationRepository.save(l1);
            sportRepository.save(s1);
        }

        User u1 = new User();
        u1.setUsername("username");
        u1.setPassword("password");
        if (userRepository.findByUsername(u1.getUsername()).isEmpty()) {
            userRepository.save(u1);
        }

        Review r1 = new Review();
        r1.setDate(LocalDate.now());
        r1.setLocation(l1);
        r1.setRating(3);
        r1.setUser(u1);
        if (reviewRepository.findAll().isEmpty()) {
            reviewRepository.save(r1);
        }
    }
}
