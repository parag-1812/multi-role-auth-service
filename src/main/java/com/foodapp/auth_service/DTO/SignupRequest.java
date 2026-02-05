package com.foodapp.auth_service.DTO;

import com.foodapp.auth_service.model.Role;

public class SignupRequest {
    private String username;
    private String password;
    private Role role;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
