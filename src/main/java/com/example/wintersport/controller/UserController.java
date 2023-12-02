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
@CrossOrigin
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody @Valid UserRequest userRequest) {
        Optional<User> user = userRepository.findByUsername(userRequest.getUsername());
        if (user.isPresent()) {
            if (passwordEncoder.matches(userRequest.getPassword(), user.get().getPassword())) {
                return "Login successful";
            }
        }
        return "Login failed";
    }
}
