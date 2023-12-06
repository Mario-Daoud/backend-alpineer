package com.example.wintersport.response;

import com.example.wintersport.domain.User;

public class UserResponse {
    private Long id;
    private String username;
    private String password;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
