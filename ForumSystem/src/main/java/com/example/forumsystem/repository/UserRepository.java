package com.example.forumsystem.repository;

import com.example.forumsystem.models.FilterUserOptions;
import com.example.forumsystem.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll(FilterUserOptions filterOptions);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
