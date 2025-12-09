package com.mindvault.mymemory.services;

import com.mindvault.mymemory.dtos.request.LoginRequest;
import com.mindvault.mymemory.dtos.request.RegisterRequest;
import com.mindvault.mymemory.dtos.response.AuthResponse;
import com.mindvault.mymemory.entities.User;
import com.mindvault.mymemory.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // 1. Check if user already exists (optional, but recommended)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered.");
        }
        
        // 2. Create a new User entity
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        
        // 3. Save the user
        userRepository.save(user);

        // 4. Generate a JWT 
        String jwt = jwtService.generateToken(user);
        
        // 5. Return the response
        return new AuthResponse(jwt, "Bearer", user.getUsername());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Authenticate using email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // 2. Find the authenticated user by email
        User user = userRepository.findByEmail(request.getEmail()) 
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        // 3. Generate a JWT
        String jwt = jwtService.generateToken(user);

        // 4. Return the response
        return new AuthResponse(jwt, "Bearer", user.getUsername());
    }
}