package com.example.wintersport.service;

import com.example.wintersport.domain.User;
import com.example.wintersport.repository.UserRepository;
import com.example.wintersport.request.UserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean LoginUser(UserRequest userRequest) {
        Optional<User> user = userRepository.findByUsername(userRequest.getUsername());
        if (user.isPresent() && passwordEncoder.matches(userRequest.getPassword(), user.get().getPassword())) {
            return true;
        }
        return false;
    }

    public User RegisterUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User UpdateUser(User userToBeUpdated, UserRequest updatedUser) {
        userToBeUpdated.setUsername(updatedUser.getUsername());
        userToBeUpdated.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        User savedUser = userRepository.save(userToBeUpdated);
        return savedUser;
    }

}
