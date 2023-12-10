package com.example.wintersport.repository;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.domain.Review;
import com.example.wintersport.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void findByLocationId() {
        User user = userRepository.save(new User("user", "password"));

        Country france = countryRepository.save(new Country("France"));
        Location alps = locationRepository.save(new Location("Alps", 100, "Alps description", 2, 100, 10, france));

        Review review = reviewRepository.save(new Review(5, user, alps));

        assertThat(reviewRepository.findByLocationId(alps.getId()).get().getFirst()).isEqualTo(review);
    }

    @Test
    public void findByLocationIdInvalid() {
        User user = userRepository.save(new User("user", "password"));

        Country france = countryRepository.save(new Country("France"));
        Location alps = locationRepository.save(new Location("Alps", 100, "Alps description", 2, 100, 10, france));

        reviewRepository.save(new Review(5, user, alps));

        assertThat(reviewRepository.findByLocationId(-1L).get().size()).isEqualTo(0);
    }

    @Test
    public void findByUserId() {
        User user = userRepository.save(new User("user", "password"));

        Country france = countryRepository.save(new Country("France"));
        Location alps = locationRepository.save(new Location("Alps", 100, "Alps description", 2, 100, 10, france));

        Review review = reviewRepository.save(new Review(5, user, alps));

        assertThat(reviewRepository.findByUserId(user.getId()).get().getFirst()).isEqualTo(review);
    }
}
