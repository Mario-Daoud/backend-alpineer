package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import com.example.wintersport.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        UserResponse userResponse = new UserResponse(user.get());
        return user.map(value -> ResponseEntity.ok(userResponse)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        UserResponse userResponse = new UserResponse(user.get());
        return user.map(value -> ResponseEntity.ok(userResponse)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> login(@RequestBody @Valid UserRequest userRequest) {
        Optional<User> user = userRepository.findByUsername(userRequest.getUsername());
        if (user.isPresent() && passwordEncoder.matches(userRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(new UserResponse(savedUser));
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.notFound().build();
    }

    // TODO: make response type
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
