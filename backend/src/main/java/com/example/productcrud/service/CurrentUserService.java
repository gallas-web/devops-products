package com.example.productcrud.service;

import com.example.productcrud.entity.User;

public interface CurrentUserService {
    User getCurrentUser();
    Long getCurrentUserId();
}
