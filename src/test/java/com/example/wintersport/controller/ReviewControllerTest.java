package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.domain.Review;
import com.example.wintersport.domain.User;
import com.example.wintersport.repository.ReviewRepository;
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
    void getReviewsByLocationIdExisting() throws Exception {
        when(reviewRepository.findByLocationId(reviews.getFirst().getLocation().getId())).thenReturn(Optional.of(reviews));

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
}
