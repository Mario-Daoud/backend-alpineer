package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CountryControllerTest {

    private final String baseUrl = "/api/country";

    @MockBean
    private CountryRepository countryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllCountriesExisting() throws Exception {
        List<Country> countries = List.of(
                new Country("Netherlands"),
                new Country("Germany"),
                new Country("France")
        );

        when(countryRepository.findAll()).thenReturn(countries);

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Netherlands"))
                .andExpect(jsonPath("$[1].name").value("Germany"))
                .andExpect(jsonPath("$[2].name").value("France"));
    }

    @Test
    public void testGetAllCountriesNonExisting() throws Exception {
        when(countryRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }
}

