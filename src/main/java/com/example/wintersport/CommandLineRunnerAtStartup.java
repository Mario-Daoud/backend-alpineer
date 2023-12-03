package com.example.wintersport;

import com.example.wintersport.domain.*;
import com.example.wintersport.repository.*;
import com.example.wintersport.response.CountryLocationsResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private BCryptPasswordEncoder passwordEncoder;

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
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public void run(String... args) throws Exception {
        if (countryRepository.findAll().isEmpty()) {
            Country c1 = new Country();
            c1.setName("France");
            countryRepository.save(c1);

            Country c2 = new Country();
            c2.setName("Italy");
            countryRepository.save(c2);

            Country c3 = new Country();
            c3.setName("Switzerland");
            countryRepository.save(c3);

            Country c4 = new Country();
            c4.setName("Austria");
            countryRepository.save(c4);
        }

        if (locationRepository.findAll().isEmpty()) {
            Location l1 = new Location();
            l1.setName("Alps");
            l1.setCountry(countryRepository.findByName("France").orElse(null));
            l1.setSnowHeight(122);
            l1.setDescription("Perfect for beginners");
            l1.setTrackLength(25);
            l1.setDegrees(-3);
            l1.setChairlifts(3);
            locationRepository.save(l1);

            Location l2 = new Location();
            l2.setName("Chamonix");
            l2.setCountry(countryRepository.findByName("France").orElse(null));
            l2.setSnowHeight(150);
            l2.setDescription("Iconic French ski resort");
            l2.setTrackLength(35);
            l2.setDegrees(-6);
            l2.setChairlifts(4);
            locationRepository.save(l2);

            Location l3 = new Location();
            l3.setName("Dolomites");
            l3.setCountry(countryRepository.findByName("Italy").orElse(null));
            l3.setSnowHeight(180);
            l3.setDescription("Stunning views and challenging slopes");
            l3.setTrackLength(30);
            l3.setDegrees(-5);
            l3.setChairlifts(5);
            locationRepository.save(l3);

            Location l4 = new Location();
            l4.setName("Sestriere");
            l4.setCountry(countryRepository.findByName("Italy").orElse(null));
            l4.setSnowHeight(160);
            l4.setDescription("Host of the 2006 Winter Olympics");
            l4.setTrackLength(28);
            l4.setDegrees(-4);
            l4.setChairlifts(3);
            locationRepository.save(l4);

            Location l5 = new Location();
            l5.setName("Zermatt");
            l5.setCountry(countryRepository.findByName("Switzerland").orElse(null));
            l5.setSnowHeight(200);
            l5.setDescription("Home to the Matterhorn");
            l5.setTrackLength(40);
            l5.setDegrees(-8);
            l5.setChairlifts(6);
            locationRepository.save(l5);

            Location l6 = new Location();
            l6.setName("Verbier");
            l6.setCountry(countryRepository.findByName("Switzerland").orElse(null));
            l6.setSnowHeight(180);
            l6.setDescription("Famous for its off-piste skiing");
            l6.setTrackLength(35);
            l6.setDegrees(-7);
            l6.setChairlifts(5);
            locationRepository.save(l6);

            Location l7 = new Location();
            l7.setName("Kitzbühel");
            l7.setCountry(countryRepository.findByName("Austria").orElse(null));
            l7.setSnowHeight(140);
            l7.setDescription("Classic Austrian ski resort");
            l7.setTrackLength(25);
            l7.setDegrees(-2);
            l7.setChairlifts(4);
            locationRepository.save(l7);

            Location l8 = new Location();
            l8.setName("St. Anton");
            l8.setCountry(countryRepository.findByName("Austria").orElse(null));
            l8.setSnowHeight(160);
            l8.setDescription("Known for challenging terrain");
            l8.setTrackLength(30);
            l8.setDegrees(-4);
            l8.setChairlifts(3);
            locationRepository.save(l8);
        }

        if (sportRepository.findAll().isEmpty()) {
            Sport s1 = new Sport();
            s1.setName("Snowboarding");
            s1.setDescription("Description for Snowboarding");
            s1.setDifficulty(3);
            Set<Location> locations1 = new HashSet<>();
            locationRepository.findByName("Dolomites").ifPresent(locations1::add);
            s1.setLocations(locations1);
            sportRepository.save(s1);

            Sport s2 = new Sport();
            s2.setName("Skiing");
            s2.setDescription("Description for Skiing");
            s2.setDifficulty(4);
            Set<Location> locations2 = new HashSet<>();
            locationRepository.findByName("Alps").ifPresent(locations2::add);
            s2.setLocations(locations2);
            sportRepository.save(s2);

            Sport s3 = new Sport();
            s3.setName("Freestyle Skiing");
            s3.setDescription("Description for Freestyle Skiing");
            s3.setDifficulty(3);
            Set<Location> locations3 = new HashSet<>();
            locationRepository.findByName("Courchevel").ifPresent(locations3::add);
            s3.setLocations(locations3);
            sportRepository.save(s3);
        }

        if (userRepository.findByUsername("username").isEmpty()) {
            User u1 = new User();
            u1.setUsername("username");
            u1.setPassword(passwordEncoder.encode("password"));
            userRepository.save(u1);

            User u2 = new User();
            u2.setUsername("user2");
            u2.setPassword(passwordEncoder.encode("password2"));
            userRepository.save(u2);
        }

        if (reviewRepository.findAll().isEmpty()) {
            Review r1 = new Review();
            r1.setDate(LocalDate.now());
            Location location1 = locationRepository.findByName("Alps").orElse(null);
            r1.setLocation(location1);
            r1.setRating(4);
            r1.setUser(userRepository.findByUsername("username").orElse(null));
            reviewRepository.save(r1);

            Review r2 = new Review();
            r2.setDate(LocalDate.now().minusDays(7));
            Location location2 = locationRepository.findByName("Dolomites").orElse(null);
            r2.setLocation(location2);
            r2.setRating(5);
            r2.setUser(userRepository.findByUsername("user2").orElse(null));
            reviewRepository.save(r2);
        }
    }

}
