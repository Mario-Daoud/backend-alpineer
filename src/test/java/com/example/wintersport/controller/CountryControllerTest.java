package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.domain.Location;
import com.example.wintersport.repository.CountryRepository;
import com.example.wintersport.repository.LocationRepository;
import com.example.wintersport.request.CountryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CountryControllerTest {

    private final String baseUrl = "/api/countries";

    @MockBean
    private CountryRepository countryRepository;
    @MockBean
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    private List<Country> countries;

    @BeforeEach
    void setUp() {
        Country country = new Country("Netherlands");
        country.setId(1L);
        Country country2 = new Country("Germany");
        country2.setId(2L);
        Country country3 = new Country("France");
        country3.setId(3L);
        countries = List.of(
                country,
                country2,
                country3
        );
    }

    @Test
    void getAllCountriesExisting() throws Exception {
        when(countryRepository.findAll()).thenReturn(countries);

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value(countries.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(countries.get(1).getName()))
                .andExpect(jsonPath("$[2].name").value(countries.get(2).getName()));
    }

    @Test
    void getAllCountriesNotExisting() throws Exception {
        when(countryRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getCountryByIdNotExisting() throws Exception {
        when(countryRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get(baseUrl + "/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createCountry() throws Exception {
        Country country = new Country("Netherlands");
        country.setId(1L);

        when(countryRepository.save(any(Country.class))).thenReturn(country);

        CountryRequest countryRequest = new CountryRequest();
        countryRequest.setName("Netherlands");

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(countryRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(header().string("Location", "http://localhost/api/country/" + country.getId()));
    }

    @Test
    void updateCountryExisting() throws Exception {
        Country country = new Country("Netherlands");
        country.setId(1L);

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(countryRepository.save(country)).thenReturn(country);

        CountryRequest countryRequest = new CountryRequest();
        countryRequest.setName("Netherlands");

        mockMvc.perform(put(baseUrl + "/" + country.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(countryRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(country.getName()));
    }

    @Test
    void updateCountryNotExisting() throws Exception {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        CountryRequest countryRequest = new CountryRequest();
        countryRequest.setName("Netherlands");

        mockMvc.perform(put(baseUrl + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(countryRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCountryNoCountryExisting() throws Exception {
        Country country = new Country("Netherlands");
        country.setId(1L);

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        mockMvc.perform(delete(baseUrl + "/" + country.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(country.getName()));
    }

    @Test
    void deleteCountryNotExisting() throws Exception {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete(baseUrl + "/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCountryWithLocations() throws Exception {
        Country country = new Country("Netherlands");
        country.setId(1L);

        Location location = new Location();
        location.setId(1L);
        location.setName("test");
        location.setChairlifts(1);
        location.setDegrees(1);
        location.setDescription("test");
        location.setSnowHeight(100);
        location.setTrackLength(100);

        country.setLocations(Set.of(location));

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        mockMvc.perform(delete(baseUrl + "/" + country.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(country.getName()));
    }
}
