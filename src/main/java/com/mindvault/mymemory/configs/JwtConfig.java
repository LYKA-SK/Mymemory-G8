package com.mindvault.mymemory.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mindvault.mymemory.entities.User;
import com.mindvault.mymemory.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final UserRepository userRepository;

    // 1. UserDetailsService Bean (Loads user by email)
 // If you need a separate bean for password updates:

    @Bean
    UserDetailsPasswordService userDetailsPasswordService(UserRepository userRepository) {
        return (userDetails, newPassword) -> {
            // Find the user by their principal name (email)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));
            
            // Update the password
            user.setPassword(newPassword); // You MUST encode this in a real scenario!
            
            return userRepository.save(user);
        };
    }

    // 2. PasswordEncoder Bean
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. AuthenticationProvider Bean
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(null);
        authProvider.setUserDetailsPasswordService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    private UserDetailsPasswordService userDetailsService() {
		// TODO Auto-generated method stub
		return null;
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	public UserRepository getUserRepository() {
		return userRepository;
	}
}