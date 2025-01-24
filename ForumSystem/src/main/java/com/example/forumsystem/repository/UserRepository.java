package com.example.forumsystem.repository;

import com.example.forumsystem.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByName(String username);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
