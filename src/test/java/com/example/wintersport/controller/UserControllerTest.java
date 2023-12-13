package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
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

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
    }

    @Test
    void loginExisting() throws Exception {
        User user = new User("test", new BCryptPasswordEncoder().encode("password"));

        when(this.userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(this.userService.loginUser(any(UserRequest.class))).thenReturn(true);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("password");

        String stringvalue = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringvalue))
                .andExpect(status().isOk());
    }

    @Test
    void loginUnauthorized() throws Exception {
        when(this.userService.loginUser(any(UserRequest.class))).thenReturn(false);

        User user = new User("test", "password");
        users.add(user);
        when(this.userRepository.findByUsername("test")).thenReturn(Optional.empty());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("unauthorized");
        userRequest.setPassword(user.getPassword());

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginEmptyUsername() throws Exception {
        users.add(new User("test", "test"));
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("test");

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }

    @Test
    void loginEmptyPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }

    @Test
    void loginEmptyUsernameAndPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }

    @Test
    void registerExisting() throws Exception {
        User user = new User("test", "test");
        user.setId(1L);

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword(user.getPassword());


        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void registerNonExisting() throws Exception {
        User user = new User("newuser", "password");
        user.setId(1L);

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        when(userService.registerUser(any(UserRequest.class))).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword(user.getPassword());

        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo("newuser")))
                .andExpect(jsonPath("$.password", equalTo("password")));
    }

    @Test
    void registerEmptyUsername() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("test");
        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }

    @Test
    void registerEmptyPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");

        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }

    @Test
    void registerEmptyUsernameAndPassword() throws Exception {
        when(this.userRepository.findByUsername("test")).thenReturn(users.stream().findFirst());
        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }

    @Test
    void updateUserExisting() throws Exception {
        User user = new User("test", "test");
        user.setId(1L);

        when(this.userRepository.save(user)).thenReturn(user);
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test2");
        userRequest.setPassword("test2");
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo("test2")))
                .andExpect(jsonPath("$.password", equalTo("test2")));
    }

    @Test
    void updateUserNonExisting() throws Exception {
        when(this.userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("nonexistent");
        userRequest.setPassword("nonexistent");

        mockMvc.perform(put(baseUrl + "/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserEmptyUsername() throws Exception {
        User user = new User("test", "test");
        user.setId(1L);

        when(this.userRepository.save(user)).thenReturn(user);
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("");
        userRequest.setPassword("test");

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateUserEmptyPassword() throws Exception {
        User user = new User("test", "test");
        user.setId(1L);

        when(this.userRepository.save(user)).thenReturn(user);
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("");

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateUserEmptyUsernameAndPassword() throws Exception {
        User user = new User("test", "test");
        user.setId(1L);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }

    @Test
    void updateUserEmptyBody() throws Exception {
        when(this.userRepository.findById(1L)).thenReturn(users.stream().findFirst());
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.userRepository);
    }
}
