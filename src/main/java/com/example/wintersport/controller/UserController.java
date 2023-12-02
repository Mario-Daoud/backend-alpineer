package com.example.wintersport.controller;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
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
    public ResponseEntity<Void> register(@RequestBody @Valid UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User(userRequest.getUsername(), passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
