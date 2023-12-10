package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
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
    private MockMvc mockMvc;

    private List<User> users;

    @BeforeEach
    public void setup() {
        User user = new User("test", "test");
        User user2 = new User("test2", "test2");
        users = List.of(user, user2);
    }

    @Test
    @WithAnonymousUser
    public void loginExisting() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + users.getFirst().getUsername() + "\", \"password\": \"" + users.getFirst().getPassword() + "\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(users.getFirst().getUsername()));
    }

    @Test
    public void loginNonExisting() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(Optional.empty()); // Assuming findByUsername returns an Optional
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + users.getFirst().getUsername() + "\", \"password\": \"" + users.getFirst().getPassword() + "\"}"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginEmptyUsername() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginEmptyPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"test\", \"password\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginEmptyUsernameAndPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\", \"password\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerExisting() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"test\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isConflict());
    }


    @Test
    public void registerNonExisting() throws Exception {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("nonexistent");
        userRequest.setPassword("test");

        when(userService.RegisterUser(userRequest)).thenReturn(new User(userRequest.getUsername(), userRequest.getPassword()));

        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"nonexistent\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(userRequest.getUsername()));
    }


    @Test
    public void registerEmptyUsername() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerEmptyPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"test\", \"password\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerEmptyUsernameAndPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\", \"password\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserExisting() throws Exception {
        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"test\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserNonExisting() throws Exception {
        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
        mockMvc.perform(put(baseUrl + "/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"test\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUserEmptyUsername() throws Exception {
        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserEmptyPassword() throws Exception {
        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"test\", \"password\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserEmptyUsernameAndPassword() throws Exception {
        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\", \"password\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserEmptyBody() throws Exception {
        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
