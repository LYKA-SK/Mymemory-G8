package com.mindvault.mymemory.services;

import com.mindvault.mymemory.dtos.request.LoginRequest;
import com.mindvault.mymemory.dtos.request.RegisterRequest;
import com.mindvault.mymemory.dtos.response.AuthResponse;
import com.mindvault.mymemory.entities.User;
import com.mindvault.mymemory.repositories.UserRepository;
import com.mindvault.mymemory.security.JwtService;

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
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 2. Create a new User entity (Assuming a working User entity with a 'builder')
        User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(encodedPassword);
                
        
                String token = jwtService.generateToken(user.getEmail());
       User saveUser = userRepository.save(user);
       return new AuthResponse(200, token, "successfully", saveUser);
        
         

//        // 4. Generate a JWT 
//        String jwt = jwtService.generateToken(user);
//        
//        // 5. Return the response
//        return new AuthResponse(jwt, "Bearer", user.getUsername());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Authenticate using email and password
        // If credentials are bad, this throws an exception caught by Spring Security (results in 403)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // 2. Find the authenticated user by email
        User user = userRepository.findByEmail(request.getEmail()) 
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        // 3. Generate a JWT
        String token = jwtService.generateToken(user.getEmail());

        // 4. Return the response
         return new AuthResponse(200, token, "successfully", user);
    }
}