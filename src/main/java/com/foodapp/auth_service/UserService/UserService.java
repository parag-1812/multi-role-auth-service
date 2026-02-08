package com.foodapp.auth_service.UserService;

import com.foodapp.auth_service.Exceptions.InvalidCredentialsException;
import com.foodapp.auth_service.Exceptions.UserNotFoundException;
import com.foodapp.auth_service.model.Role;
import com.foodapp.auth_service.model.User;
import com.foodapp.auth_service.UserRepository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Of UserService
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String rawPassword, Role role) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, encodedPassword, role);
        return userRepository.save(user);
    }

    public User authenticate(String username, String rawPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return user;
    }
}
