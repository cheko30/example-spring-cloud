package com.spo.user.service;

import com.spo.user.entities.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();

    User getUser(String userId);
}
