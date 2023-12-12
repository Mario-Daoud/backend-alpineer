package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.response.UserResponse;
import com.example.wintersport.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> login(@RequestBody @Valid UserRequest userRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode(userRequest.getPassword()));
        if (userService.loginUser(userRequest)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User savedUser = userService.registerUser(userRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(new UserResponse(savedUser));
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(name = "userId") Long id, @RequestBody UserRequest userRequest) {
        if (userRequest.getPassword().isBlank() && userRequest.getUsername().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User userToUpdate = existingUser.get();
        if (userRequest.getPassword().isBlank()) {
            userToUpdate.setUsername(userToUpdate.getUsername());
        } else {
            userToUpdate.setUsername(userRequest.getUsername());
        }

        if (userRequest.getUsername().isBlank()) {
            userToUpdate.setPassword(userToUpdate.getPassword());
        } else {
            userToUpdate.setPassword(userRequest.getPassword());
        }

        User updatedUser = userRepository.save(userToUpdate);
        return ResponseEntity.ok(new UserResponse(updatedUser));
    }
}
