package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.response.UserResponse;
import com.example.wintersport.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return userRepository.findByUsername(userRequest.getUsername())
                .map(user -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access"));
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User savedUser = userService.RegisterUser(userRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(new UserResponse(savedUser));
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(name = "userId") Long id, @RequestBody UserRequest userRequest) {
        return userRepository.findById(id)
                .map(userToUpdate -> {
                    userToUpdate.setUsername(userRequest.getUsername());
                    userToUpdate.setPassword(userRequest.getPassword());
                    User updatedUser = userRepository.save(userToUpdate);
                    return ResponseEntity.ok(new UserResponse(updatedUser));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
