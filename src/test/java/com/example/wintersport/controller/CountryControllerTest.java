package com.example.wintersport.controller;

import com.example.wintersport.domain.Country;
import com.example.wintersport.repository.CountryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(CountryController.class)
public class CountryControllerTest {
    private final String baseUrl = "/api/country";
    @MockBean
    private CountryRepository countryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllCountriesExisting() throws Exception {
        List<Country> countries = new ArrayList<>();

        countries.add(new Country("Netherlands"));
        countries.add(new Country("Germany"));
        countries.add(new Country("France"));

        when(this.countryRepository.findAll()).thenReturn(countries);

        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Netherlands"))
                .andExpect(jsonPath("$[1].name").value("Germany"))
                .andExpect(jsonPath("$[2].name").value("France"));

    }
}
