package com.mindvault.mymemory.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority; // Required Import
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Required Import
import org.springframework.security.core.userdetails.UserDetails; // Required Import

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
// CRITICAL FIX: Implement UserDetails
public class User implements UserDetails { 

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    
    @Column(unique = true, nullable = false, name = "user_name")
    private String username;

    // -----------------------------------------------------------------
    // METHODS REQUIRED BY UserDetails INTERFACE (The Fix for ClassCastException)
    // -----------------------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Since your user model doesn't show roles, we assign a default "USER" role.
        // If you had roles, you would map them here.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // Returns the password field for comparison during authentication.
        return this.password;
    }

    @Override
    public String getUsername() {
        // CRITICAL: Spring Security uses this for the unique identifier. 
        // We use 'email' as it is your unique identifier field.
        return this.email; 
    }

    // Default implementations for account status checks:
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Assuming the user is active once created. 
        return true;
    }
}