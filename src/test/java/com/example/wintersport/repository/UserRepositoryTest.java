package com.example.wintersport.repository;

import com.example.wintersport.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameExisting() {
        userRepository.save(new User("username", "password"));

        assertThat(userRepository.findByUsername("username").get().getUsername()).isEqualTo("username");
    }

    @Test
    public void findByUsernameNotExisting() {
        assertThat(userRepository.findByUsername("username")).isEmpty();
    }

    @Test
    public void findByUsernameEmpty() {
        assertThat(userRepository.findByUsername("")).isEmpty();
    }

    @Test
    public void findByUsernameNull() {
        assertThat(userRepository.findByUsername(null)).isEmpty();
    }

    @Test
    public void findByUsernameBlank() {
        assertThat(userRepository.findByUsername(" ")).isEmpty();
    }
}
