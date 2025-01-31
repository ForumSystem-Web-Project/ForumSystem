package com.example.forumsystem.service;

import com.example.forumsystem.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    void createUser(User user);

    void updateUser(User user, User modifier, int id);

    void deleteUser(int id);
}
