package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    private final String baseUrl = "/api/user";
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private List<User> users;

    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void loginExisting() throws Exception {
        User user = new User("test", new BCryptPasswordEncoder().encode("password"));
        when(this.userRepository.save(user)).thenReturn(user);
        when(this.userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("password");

        String stringvalue = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringvalue))
                .andExpect(status().isOk());
    }
//
//    @Test
//    public void loginNonExisting() throws Exception {
//        when(this.userRepository.findByUsername("test")).thenReturn(Optional.empty());
//        mockMvc.perform(post(baseUrl + "/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"" + users.getFirst().getUsername() + "\", \"password\": \"" + users.getFirst().getPassword() + "\"}"))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void loginEmptyUsername() throws Exception {
//        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
//        mockMvc.perform(post(baseUrl + "/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"\", \"password\": \"test\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void loginEmptyPassword() throws Exception {
//        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
//        mockMvc.perform(post(baseUrl + "/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"test\", \"password\": \"\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void loginEmptyUsernameAndPassword() throws Exception {
//        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
//        mockMvc.perform(post(baseUrl + "/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"\", \"password\": \"\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }

    @Test
    public void registerExisting() throws Exception {
        User user = new User("test", "test");
        user.setId(40L);

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword(user.getPassword());

        String stringvalue = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringvalue))
                .andDo(print())
                .andExpect(status().isConflict());
    }


    @Test
    public void registerNonExisting() throws Exception {
        User user = new User("newuser", "password");
        user.setId(40L);

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        when(userService.RegisterUser(any(UserRequest.class))).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword(user.getPassword());

        String stringvalue = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringvalue))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(40)))
                .andExpect(jsonPath("$.username", equalTo("newuser")))
                .andExpect(jsonPath("$.password", equalTo("password")));
    }
//
//    @Test
//    public void registerEmptyUsername() throws Exception {
//        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
//        mockMvc.perform(post(baseUrl + "/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"\", \"password\": \"test\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void registerEmptyPassword() throws Exception {
//        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
//        mockMvc.perform(post(baseUrl + "/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"test\", \"password\": \"\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void registerEmptyUsernameAndPassword() throws Exception {
//        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
//        mockMvc.perform(post(baseUrl + "/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"\", \"password\": \"\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void updateUserExisting() throws Exception {
//        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
//        User newUser = new User("test", "test");
//
//        String userJson = "{\"username\": \"" + newUser.getUsername() + "\", \"password\": \"" + newUser.getPassword() + "\"}";
//        when(this.userRepository.save(newUser)).thenReturn(newUser);
//
//        mockMvc.perform(put(baseUrl + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson));
//    }
//
//    @Test
//    public void updateUserNonExisting() throws Exception {
//        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
//        mockMvc.perform(put(baseUrl + "/3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"test\", \"password\": \"test\"}"))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void updateUserEmptyUsername() throws Exception {
//        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
//        mockMvc.perform(put(baseUrl + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"\", \"password\": \"test\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void updateUserEmptyPassword() throws Exception {
//        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
//        mockMvc.perform(put(baseUrl + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"test\", \"password\": \"\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void updateUserEmptyUsernameAndPassword() throws Exception {
//        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
//        mockMvc.perform(put(baseUrl + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\": \"\", \"password\": \"\"}"))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void updateUserEmptyBody() throws Exception {
//        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
//        mockMvc.perform(put(baseUrl + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(""))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
}
