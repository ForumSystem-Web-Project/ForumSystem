package com.example.forumsystem.service;

import com.example.forumsystem.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByName(String username);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
