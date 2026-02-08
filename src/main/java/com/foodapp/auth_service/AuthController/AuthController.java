package com.foodapp.auth_service.AuthController;

import com.foodapp.auth_service.DTO.AuthResponse;
import com.foodapp.auth_service.DTO.JwtResponse;
import com.foodapp.auth_service.DTO.LoginRequest;
import com.foodapp.auth_service.DTO.SignupRequest;
import com.foodapp.auth_service.model.User;
import com.foodapp.auth_service.UserService.UserService;
import com.foodapp.auth_service.security.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        userService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getRole()
        );
        return new AuthResponse("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.authenticate(
                    request.getUsername(),
                    request.getPassword()
            );

            String token = JwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole().name()
            );

            return ResponseEntity.ok(new JwtResponse(token));

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(ex.getMessage()));
        }
    }

}

