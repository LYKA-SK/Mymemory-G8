package com.mindvault.mymemory.services;

import com.mindvault.mymemory.dtos.request.LoginRequest;
import com.mindvault.mymemory.dtos.request.RegisterRequest;
import com.mindvault.mymemory.dtos.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}