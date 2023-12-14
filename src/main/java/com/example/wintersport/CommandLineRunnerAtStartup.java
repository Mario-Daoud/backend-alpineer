package com.example.wintersport;

import com.example.wintersport.domain.*;
import com.example.wintersport.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerAtStartup implements CommandLineRunner {

    private CountryRepository countryRepository;
    private LocationRepository locationRepository;
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public CommandLineRunnerAtStartup(CountryRepository countryRepository,
                                      LocationRepository locationRepository,
                                      UserRepository userRepository,
                                      ReviewRepository reviewRepository) {
        this.countryRepository = countryRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public void run(String... args) throws Exception {
        // add countries
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

        Country c5 = new Country();
        c5.setName("Germany");
        countryRepository.save(c5);

        Country c6 = new Country();
        c6.setName("USA");
        countryRepository.save(c6);

        Country c7 = new Country();
        c7.setName("Canada");
        countryRepository.save(c7);

        Country c8 = new Country();
        c8.setName("Japan");
        countryRepository.save(c8);

        Country c9 = new Country();
        c9.setName("Sweden");
        countryRepository.save(c9);

        Country c10 = new Country();
        c10.setName("Norway");
        countryRepository.save(c10);

        // add user
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);


        // add locations
        addLocationAndReviews("Alps", "Perfect for beginners", 122, 25, -3, 3, c1, user);
        addLocationAndReviews("Chamonix", "Iconic French ski resort", 150, 35, -6, 4, c1, user);
        addLocationAndReviews("French Riviera", "Coastal skiing experience", 100, 20, 2, 2, c1, user);

        addLocationAndReviews("Dolomites", "Stunning views and challenging slopes", 180, 30, -5, 5, c2, user);
        addLocationAndReviews("Sestriere", "Host of the 2006 Winter Olympics", 160, 28, -4, 3, c2, user);
        addLocationAndReviews("Cortina d'Ampezzo", "Italy's most famous ski resort", 140, 25, -3, 3, c2, user);

        addLocationAndReviews("Zermatt", "Home to the Matterhorn", 200, 40, -8, 6, c3, user);
        addLocationAndReviews("Verbier", "Famous for its off-piste skiing", 180, 35, -7, 5, c3, user);
        addLocationAndReviews("Swiss Alps", "Scenic beauty and diverse terrain", 170, 32, -6, 4, c3, user);

        addLocationAndReviews("Kitzbühel", "Classic Austrian ski resort", 140, 25, -2, 4, c4, user);
        addLocationAndReviews("St. Anton", "Known for challenging terrain", 160, 30, -4, 3, c4, user);
        addLocationAndReviews("Innsbruck", "Historic ski destination", 120, 22, -1, 2, c4, user);

        addLocationAndReviews("Garmisch-Partenkirchen", "Bavarian ski resort", 140, 28, -3, 3, c5, user);
        addLocationAndReviews("Black Forest", "Wooded slopes and cultural charm", 110, 18, 1, 2, c5, user);
        addLocationAndReviews("Bavarian Alps", "Traditional skiing experience", 130, 24, -1, 2, c5, user);

        addLocationAndReviews("Aspen", "Famous American skiing destination", 180, 40, -8, 6, c6, user);
        addLocationAndReviews("Vail", "Largest ski resort in Colorado", 160, 35, -7, 5, c6, user);
        addLocationAndReviews("Lake Tahoe", "California-Nevada border skiing", 150, 30, -6, 4, c6, user);

        addLocationAndReviews("Banff", "Canadian Rockies beauty", 160, 35, -7, 5, c7, user);
        addLocationAndReviews("Whistler", "Largest ski resort in North America", 200, 40, -8, 6, c7, user);
        addLocationAndReviews("Jasper", "Alberta's tranquil skiing destination", 140, 25, -6, 4, c7, user);

        addLocationAndReviews("Niseko", "Japan's most famous ski resort", 200, 30, -5, 5, c8, user);
        addLocationAndReviews("Hakuba", "Japanese Alps skiing experience", 160, 28, -4, 3, c8, user);
        addLocationAndReviews("Shiga Kogen", "Largest ski area in Japan", 180, 32, -5, 4, c8, user);

        addLocationAndReviews("Åre", "Sweden's largest ski resort", 140, 25, -2, 4, c9, user);
        addLocationAndReviews("Sälen", "Scandinavia's largest ski area", 120, 22, -3, 3, c9, user);
        addLocationAndReviews("Vemdalen", "Family-friendly skiing in Sweden", 130, 24, -1, 2, c9, user);

        addLocationAndReviews("Hemsedal", "Norway's most popular ski resort", 160, 30, -4, 3, c10, user);
        addLocationAndReviews("Trysil", "Largest ski resort in Norway", 150, 28, -3, 3, c10, user);
        addLocationAndReviews("Geilo", "Traditional Norwegian skiing", 130, 24, -2, 2, c10, user);
    }

    private void addLocationAndReviews(String name, String description, int snowHeight, int trackLength,
                                       int degrees, int chairlifts, Country country, User user) {
        Location location = new Location();
        location.setName(name);
        location.setDescription(description);
        location.setSnowHeight(snowHeight);
        location.setTrackLength(trackLength);
        location.setDegrees(degrees);
        location.setChairlifts(chairlifts);
        location.setCountry(country);
        locationRepository.save(location);

        for (int i = 0; i <= 3; i++) {
            Review review = new Review();
            review.setLocation(location);
            review.setRating((int) (Math.random() * 5) + 1);
            review.setUser(userRepository.save(user));
            reviewRepository.save(review);
        }
    }
}
