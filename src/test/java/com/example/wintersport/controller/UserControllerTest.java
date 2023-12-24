package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.response.UserResponse;
import com.example.wintersport.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private final String baseUrl = "/api/users";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    void getUserByIdExisting() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(get(baseUrl + "/" + user.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo(user.getUsername())))
                .andExpect(jsonPath("$.password", equalTo(user.getPassword())));
    }

    @Test
    void getUserByIdNotExisting() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        mockMvc.perform(get(baseUrl + "/" + user.getUsername()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void loginExisting() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userService.loginUser(any(UserRequest.class))).thenReturn(true);

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserRequest("test")))
                .andExpect(status().isOk());
    }

    @Test
    void loginUnauthorized() throws Exception {
        when(userService.loginUser(any(UserRequest.class))).thenReturn(false);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserRequest("unauthorized")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginEmptyUsername() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userService.loginUser(any(UserRequest.class))).thenReturn(true);

        UserRequest userRequest = new UserRequest();
        userRequest.setPassword(user.getPassword());

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userRepository);
    }

    @Test
    void loginEmptyPassword() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userService.loginUser(any(UserRequest.class))).thenReturn(true);
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());

        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userRepository);
    }

    @Test
    void loginEmptyUsernameAndPassword() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userService.loginUser(any(UserRequest.class))).thenReturn(true);
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userRepository);
    }

    @Test
    void registerExisting() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

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
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

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
                .andExpect(jsonPath("$.username", equalTo(user.getUsername())))
                .andExpect(jsonPath("$.password", equalTo(user.getPassword())));
    }

    @Test
    void registerEmptyUsername() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword(user.getPassword());

        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userRepository);
    }

    @Test
    void registerEmptyPassword() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());

        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userRepository);
    }

    @Test
    void registerEmptyUsernameAndPassword() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUserExisting() throws Exception {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test2");
        userRequest.setPassword("test2");

        User newUser = new User("test2", "test2");
        newUser.setId(user.getId());

        when(userService.updateUser(any(User.class), any(UserRequest.class))).thenReturn(new UserResponse(newUser));

        mockMvc.perform(put(baseUrl + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo(userRequest.getUsername())))
                .andExpect(jsonPath("$.password", equalTo(userRequest.getPassword())));
    }

    @Test
    void updateUserNonExisting() throws Exception {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
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
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("");
        userRequest.setPassword("test2");

        mockMvc.perform(put(baseUrl + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserEmptyPassword() throws Exception {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("");

        mockMvc.perform(put(baseUrl + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserEmptyUsernameAndPassword() throws Exception {
        when(userRepository.save(user)).thenReturn(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(put(baseUrl + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateUserEmptyBody() throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        mockMvc.perform(put(baseUrl + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    private User createUser() {
        User user = new User("test", "password");
        user.setId(1L);
        return user;
    }

    private String createUserRequest(String username) throws JsonProcessingException {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword("password");
        return objectMapper.writeValueAsString(userRequest);
    }
}
