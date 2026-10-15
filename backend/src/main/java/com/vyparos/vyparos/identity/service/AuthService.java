package com.vyparos.vyparos.identity.service;

import com.vyparos.vyparos.identity.dto.AuthResponse;
import com.vyparos.vyparos.identity.dto.LoginRequest;
import com.vyparos.vyparos.identity.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse getCurrentUser(Long userId);
}
