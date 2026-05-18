package com.example.productcrud.service;

import com.example.productcrud.dto.AuthResponse;
import com.example.productcrud.dto.*;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserDto getCurrentUser();
    void logout();
}
