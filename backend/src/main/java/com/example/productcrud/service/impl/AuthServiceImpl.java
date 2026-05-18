package com.example.productcrud.service.impl;

import com.example.productcrud.config.JwtTokenProvider;
import com.example.productcrud.dto.*;
import com.example.productcrud.entity.Cart;
import com.example.productcrud.entity.User;
import com.example.productcrud.repository.CartRepository;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(User.UserRole.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // Créer un panier pour le nouvel utilisateur
        Cart cart = Cart.builder()
                .user(user)
                .build();
        cartRepository.save(cart);

        String token = tokenProvider.generateTokenFromUsername(user.getEmail());
        long expirationTime = System.currentTimeMillis() + tokenProvider.getJwtExpirationMs();
        LocalDateTime expiresAt = new Date(expirationTime).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        UserDto userDto = mapToUserDto(user);

        return AuthResponse.builder()
                .token(token)
                .expiresAt(expiresAt)
                .user(userDto)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (!user.getActive()) {
            throw new RuntimeException("Ce compte a été désactivé");
        }

        String token = tokenProvider.generateTokenFromUsername(user.getEmail());
        long expirationTime = System.currentTimeMillis() + tokenProvider.getJwtExpirationMs();
        LocalDateTime expiresAt = new Date(expirationTime).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        UserDto userDto = mapToUserDto(user);

        return AuthResponse.builder()
                .token(token)
                .expiresAt(expiresAt)
                .user(userDto)
                .build();
    }

    @Override
    public UserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur actuel non trouvé"));
        return mapToUserDto(user);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .zipCode(user.getZipCode())
                .country(user.getCountry())
                .role(user.getRole().toString())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
