package com.foodapp.auth_service.AuthController;

import com.foodapp.auth_service.DTO.AuthResponse;
import com.foodapp.auth_service.DTO.LoginRequest;
import com.foodapp.auth_service.DTO.SignupRequest;
import com.foodapp.auth_service.model.User;
import com.foodapp.auth_service.UserService.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getRole()
        );
        userService.save(user);
        return new AuthResponse("User registered successfully");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        userService.authenticate(request.getUsername(), request.getPassword());
        return new AuthResponse("Login successful");
    }
}

