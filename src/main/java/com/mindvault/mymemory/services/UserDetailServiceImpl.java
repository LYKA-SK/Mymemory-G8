package com.mindvault.mymemory.services;

import com.mindvault.mymemory.repositories.UserRepository;
import com.mindvault.mymemory.entities.User; // Assuming User is your entity
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email) 
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
