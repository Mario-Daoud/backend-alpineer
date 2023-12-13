package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.domain.Review;
import com.example.wintersport.domain.User;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.repository.ReviewRepository;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.ReviewRequest;
import com.example.wintersport.response.ReviewResponse;
import com.example.wintersport.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {
    private final String baseUrl = "/api/review";
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private ReviewService reviewService;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CountryRepository countryRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private List<Review> reviews;
    private List<User> users;
    private List<Location> locations;
    private List<Country> countries;

    @BeforeEach
    void setUp() {
        countries = new ArrayList<>();
        reviews = new ArrayList<>();
        users = new ArrayList<>();
        locations = new ArrayList<>();
    }

    @Test
    void getAllReviewsExisting() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");

        Location location = new Location();
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setId(1L);

        location.setCountry(country);
        country.setLocations(Set.of(location));

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);
        reviews.add(review);

        when(reviewRepository.findAll()).thenReturn(reviews);

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].user").value(user.getUsername()))
                .andExpect(jsonPath("$[0].location").value(location.getName()));
    }

    @Test
    void getAllReviewsNonExisting() throws Exception {
        when(reviewRepository.findAll()).thenReturn(reviews);

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getReviewsByLocationIdExisting() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");

        Location location = new Location();
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setId(1L);

        location.setCountry(country);
        country.setLocations(Set.of(location));

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);
        reviews.add(review);
        location.setReviews(Set.of(review));

        when(reviewRepository.findByLocationId(1L)).thenReturn(Optional.of(reviews));

        mockMvc.perform(get(baseUrl + "/location/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].username").value(user.getUsername()));
    }

    @Test
    void getReviewsByLocationIdNonExisting() throws Exception {
        when(reviewRepository.findByLocationId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/location/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getReviewsByUserIdExisting() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");

        Location location = new Location();
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setId(1L);

        location.setCountry(country);
        country.setLocations(Set.of(location));

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);
        reviews.add(review);

        when(reviewRepository.findByUserId(1L)).thenReturn(Optional.of(reviews));

        mockMvc.perform(get(baseUrl + "/user/" + user.getId()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].location").value(location.getName()));
    }

    @Test
    void getReviewsByUserIdNonExisting() throws Exception {
        when(reviewRepository.findByUserId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/user/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getReviewByIdExisting() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");

        Location location = new Location();
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setId(1L);

        location.setCountry(country);
        country.setLocations(Set.of(location));

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);
        reviews.add(review);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(get(baseUrl + "/" + review.getId()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(review.getRating()))
                .andExpect(jsonPath("$.user").value(user.getUsername()))
                .andExpect(jsonPath("$.location").value(location.getName()));
    }

    @Test
    void getReviewByIdNonExisting() throws Exception {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void addReview() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");

        Location location = new Location();
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setId(1L);

        location.setCountry(country);
        country.setLocations(Set.of(location));

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);

        when(countryRepository.save(any(Country.class))).thenReturn(country);
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(reviewService.addReview(any(Long.class), any(Long.class), any(ReviewRequest.class))).thenReturn(new ReviewResponse(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(5);

        mockMvc.perform(post("/api/review/{userId}/{locationId}", user.getId(), location.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("location", "http://localhost/api/review/1/1/1"))
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andExpect(jsonPath("$.rating").value(review.getRating()));
    }

    @Test
    void addReviewNonExistingUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(5);

        mockMvc.perform(post("/api/review/{userId}/{locationId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addReviewNonExistingLocation() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(5);

        mockMvc.perform(post("/api/review/{userId}/{locationId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReviewExisting() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");

        Location location = new Location();
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setId(1L);

        location.setCountry(country);
        country.setLocations(Set.of(location));

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(3);
        review.setUser(user);
        review.setLocation(location);
        reviews.add(review);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 5}"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andExpect(jsonPath("$.rating").value(review.getRating()));
    }

    @Test
    void updateReviewNonExisting() throws Exception {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 5}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReviewExisting() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");

        Location location = new Location();
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setId(1L);

        location.setCountry(country);
        country.setLocations(Set.of(location));

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);
        reviews.add(review);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(delete(baseUrl + "/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andExpect(jsonPath("$.rating").value(review.getRating()));
    }

    @Test
    void deleteReviewNonExisting() throws Exception {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete(baseUrl + "/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
