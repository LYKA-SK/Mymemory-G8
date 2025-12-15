package com.mindvault.mymemory.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Import this
import org.springframework.security.core.context.SecurityContextHolder; // Import this
import org.springframework.security.core.userdetails.UserDetails; // Import this
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Import this
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mindvault.mymemory.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        
        // 1. Check for token presence and format
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String email = null;
        
        try {
            // 2. Extract email (This might throw ExpiredJwtException, which is why we use try-catch)
            email = jwtService.extractEmail(token);
        } catch (Exception e) {
            // If the token is invalid or expired, Spring Security will handle the 403/401 later 
            // if no authentication is set. We proceed and let the AuthEntryPointJwt handle it.
        }


        // 3. Validate Token and User, AND check if authentication is ALREADY set
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Assuming User implements UserDetails
            UserDetails userDetails = (UserDetails) userRepository.findByEmail(email).orElse(null);
            
            if (userDetails != null && jwtService.validateToken(token, userDetails.getUsername())) {
                
                // 4. Create Authentication object
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities() // Get roles/authorities from UserDetails
                );

                // 5. Add request details
                authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6. CRITICAL STEP: Set the Security Context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 7. Continue the chain
        filterChain.doFilter(request, response);
    }
}