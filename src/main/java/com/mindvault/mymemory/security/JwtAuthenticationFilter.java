//package com.mindvault.mymemory.configs;
//
//import com.mindvault.mymemory.services.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//        
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String userEmail; 
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(jwt); 
//
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//            
//            if (jwtService.isTokenValid(jwt, userDetails)) {
//                
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null, 
//                        userDetails.getAuthorities()
//                );
//                
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        
//        filterChain.doFilter(request, response);
//    }
//}


package com.mindvault.mymemory.security;




import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mindvault.mymemory.entities.User;
import com.mindvault.mymemory.repositories.UserRepository;

import java.io.IOException;

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
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String email = jwtService.extractEmail(token);

        if (email != null && jwtService.validateToken(token, email)) {
            // You can store user info in request attribute for controllers
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                request.setAttribute("user", user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
