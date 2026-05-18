package com.example.productcrud.service;

import com.example.productcrud.dto.UserDto;

public interface UserService {
    UserDto getUserById(Long userId);
    UserDto updateUser(Long userId, UserDto userDto);
    UserDto getUserByEmail(String email);
    void deleteUser(Long userId);
}
