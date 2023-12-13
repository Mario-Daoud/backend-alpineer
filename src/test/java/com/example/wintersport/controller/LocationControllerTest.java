package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.response.LocationCountryResponse;
import com.example.wintersport.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@WebMvcTest(LocationController.class)
@AutoConfigureMockMvc(addFilters = false)
class LocationControllerTest {

    private final String baseUrl = "/api/location";

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private LocationService locationService;

    @MockBean
    private CountryRepository countryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllLocationsWithCountriesNonExisting() throws Exception {
        when(locationRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllLocationsWithCountriesExisting() throws Exception {
        Country country = createCountry();
        Location location = createLocation(country);

        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(locationRepository.findAll()).thenReturn(List.of(location));

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(location.getName()))
                .andExpect(jsonPath("$[0].country").value(country.getName()));
    }

    @Test
    void getFeaturedLocationNonExisting() throws Exception {
        when(locationService.getFeaturedLocations()).thenReturn(new HashSet<>());

        mockMvc.perform(get(baseUrl + "/featured"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getFeaturedLocationExisting() throws Exception {
        Set<LocationCountryResponse> featuredLocations = new HashSet<>();
        Country country = createCountry();
        Location location = createLocation(country);

        featuredLocations.add(new LocationCountryResponse(location));

        when(locationService.getFeaturedLocations()).thenReturn(featuredLocations);

        mockMvc.perform(get(baseUrl + "/featured"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[0].country").value("test"));
    }

    @Test
    void getLocationsByCountryNameNonExisting() throws Exception {
        when(locationRepository.findByCountryName("test")).thenReturn(new ArrayList<>());

        mockMvc.perform(get(baseUrl + "/country/" + "test"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getLocationsByCountryNameExisting() throws Exception {
        Country country = createCountry();
        Location location = createLocation(country);

        when(countryRepository.findByName(country.getName())).thenReturn(Optional.of(country));

        when(locationRepository.findByCountryName(country.getName())).thenReturn(List.of(location));

        mockMvc.perform(get(baseUrl + "/country/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(location.getName()))
                .andExpect(jsonPath("$[0].country").value(country.getName()));
    }

    @Test
    void getLocationsByCountryIdExistingNoLocations() throws Exception {
        Country country = createCountry();

        when(locationRepository.findByCountryName(country.getName())).thenReturn(new ArrayList<>());

        mockMvc.perform(get(baseUrl + "/country/" + country.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getLocationsByCountryIdExistingNoCountry() throws Exception {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/country/1" ))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Country createCountry() {
        Country country = new Country();
        country.setId(1L);
        country.setName("test");
        return country;
    }

    private Location createLocation(Country country) {
        Location location = new Location();
        location.setId(1L);
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);
        location.setCountry(country);
        return location;
    }
}
