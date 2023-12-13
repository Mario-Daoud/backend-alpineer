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
import com.fasterxml.jackson.core.JsonProcessingException;
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
        createTestData();
    }

    @Test
    void getAllReviewsExisting() throws Exception {
        when(reviewRepository.findAll()).thenReturn(reviews);

        mockMvc.perform(get(baseUrl))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(reviews.getFirst().getRating()))
                .andExpect(jsonPath("$[0].user").value(users.getFirst().getUsername()))
                .andExpect(jsonPath("$[0].location").value(locations.getFirst().getName()));
    }

    @Test
    void getAllReviewsNonExisting() throws Exception {
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(baseUrl))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getReviewsByLocationIdExisting() throws Exception {
        when(reviewRepository.findByLocationId(reviews.getFirst().getId())).thenReturn(Optional.of(Collections.singletonList(reviews.getFirst())));

        mockMvc.perform(get(baseUrl + "/location/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(reviews.getFirst().getRating()))
                .andExpect(jsonPath("$[0].username").value(users.getFirst().getUsername()));
    }

    @Test
    void getReviewsByLocationIdNonExisting() throws Exception {
        when(reviewRepository.findByLocationId(reviews.getFirst().getId())).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/location/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReviewsByUserIdExisting() throws Exception {
        when(reviewRepository.findByUserId(reviews.getFirst().getId())).thenReturn(Optional.of(Collections.singletonList(reviews.getFirst())));

        mockMvc.perform(get(baseUrl + "/user/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(reviews.getFirst().getRating()))
                .andExpect(jsonPath("$[0].location").value(locations.getFirst().getName()));
    }

    @Test
    void getReviewsByUserIdNonExisting() throws Exception {
        when(reviewRepository.findByUserId(reviews.getFirst().getId())).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReviewByIdExisting() throws Exception {
        when(reviewRepository.findById(reviews.getFirst().getId())).thenReturn(Optional.of(reviews.getFirst()));

        mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(reviews.getFirst().getRating()))
                .andExpect(jsonPath("$.user").value(users.getFirst().getUsername()))
                .andExpect(jsonPath("$.location").value(locations.getFirst().getName()));
    }

    @Test
    void getReviewByIdNonExisting() throws Exception {
        when(reviewRepository.findById(reviews.getFirst().getId())).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addReview() throws Exception {
        when(countryRepository.save(any(Country.class))).thenReturn(countries.getFirst());
        when(locationRepository.save(any(Location.class))).thenReturn(locations.getFirst());
        when(reviewService.addReview(any(Long.class), any(Long.class), any(ReviewRequest.class)))
                .thenReturn(new ReviewResponse(reviews.getFirst()));
        when(reviewRepository.save(any(Review.class))).thenReturn(reviews.getFirst());

        mockMvc.perform(post("/api/review/{userId}/{locationId}", users.getFirst().getId(), locations.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewRequest()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("location", "http://localhost/api/review/1/1/1"))
                .andExpect(jsonPath("$.id").value(reviews.getFirst().getId()))
                .andExpect(jsonPath("$.rating").value(reviews.getFirst().getRating()));
    }

    @Test
    void addReviewNonExistingUser() throws Exception {
        when(userRepository.findById(users.getFirst().getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/review/{userId}/{locationId}", users.getFirst().getId(), locations.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewRequest()))
                .andExpect(status().isNotFound());
    }

    @Test
    void addReviewNonExistingLocation() throws Exception {
        when(userRepository.findById(users.getFirst().getId())).thenReturn(Optional.of(users.getFirst()));
        when(locationRepository.findById(locations.getFirst().getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/review/{userId}/{locationId}", users.getFirst().getId(), locations.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewRequest()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReviewExisting() throws Exception {
        when(reviewRepository.findById(reviews.getFirst().getId())).thenReturn(Optional.of(reviews.getFirst()));

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewRequest()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviews.getFirst().getId()))
                .andExpect(jsonPath("$.rating").value(reviews.getFirst().getRating()));
    }

    @Test
    void updateReviewNonExisting() throws Exception {
        when(reviewRepository.findById(reviews.getFirst().getId())).thenReturn(Optional.empty());

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewRequest()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReviewExisting() throws Exception {
        when(userRepository.findById(users.getFirst().getId())).thenReturn(Optional.of(users.getFirst()));
        when(locationRepository.findById(locations.getFirst().getId())).thenReturn(Optional.of(locations.getFirst()));
        when(reviewRepository.findById(reviews.getFirst().getId())).thenReturn(Optional.of(reviews.getFirst()));

        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviews.getFirst().getId()))
                .andExpect(jsonPath("$.rating").value(reviews.getFirst().getRating()));
    }

    @Test
    void deleteReviewNonExisting() throws Exception {
        when(reviewRepository.findById(reviews.getFirst().getId())).thenReturn(Optional.empty());

        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isNotFound());
    }

    private void createTestData() {
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

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setUser(user);
        review.setLocation(location);

        countries.add(country);
        locations.add(location);
        users.add(user);
        reviews.add(review);
    }

    String createReviewRequest() throws JsonProcessingException {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(5);
        return objectMapper.writeValueAsString(reviewRequest);
    }
}

