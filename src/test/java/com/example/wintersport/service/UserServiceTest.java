package com.example.wintersport.service;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void loginCorrect() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("password");

        boolean result = userService.loginUser(userRequest);

        assertThat(result).isTrue();
    }

    @Test
    void loginIncorrect() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("wrongpassword");

        boolean result = userService.loginUser(userRequest);

        assertThat(result).isFalse();
    }


    @Test
    void registerUserNonExisisting() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("password");

        User result = userService.registerUser(userRequest);

        assertThat(result).isEqualTo(user);
    }


    @Test
    void registerUserInvalid() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("password");

        User result = userService.registerUser(userRequest);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void registerUserEmptyUsername() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("password");

        User result = userService.registerUser(userRequest);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void registerUserEmptyPassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode(""));

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("");

        User result = userService.registerUser(userRequest);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void registerUserEmptyUsernameAndPassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("");
        user.setPassword(passwordEncoder.encode(""));

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword("");

        User result = userService.registerUser(userRequest);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void updateUserExisting() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setPassword(passwordEncoder.encode("oldPassword"));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("newUsername");
        userRequest.setPassword("newPassword");

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(existingUser);

        UserResponse result = userService.updateUser(existingUser, userRequest);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(result.getPassword()).isEqualTo(existingUser.getPassword());
    }

    @Test
    void updateUserNonExisting() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setPassword(passwordEncoder.encode("oldPassword"));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("newUsername");
        userRequest.setPassword("newPassword");

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(existingUser);

        UserResponse result = userService.updateUser(existingUser, userRequest);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(result.getPassword()).isEqualTo(existingUser.getPassword());
    }
}
