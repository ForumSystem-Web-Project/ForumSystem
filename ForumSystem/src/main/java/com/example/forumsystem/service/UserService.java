package com.example.forumsystem.service;

import com.example.forumsystem.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(User modifier, int id);

    User getByUsername(User admin, String username);

    User getByEmail(User admin, String email);

    User getByFirstName(User admin, String firstName);

    void createUser(User user);

    void updateUser(User user, User modifier);

    void deleteUser(User modifier, int id);

    void makeAdmin (User admin, int id);

    void removeAdmin (User admin, int id);

    void blockUser (User admin, int id);

    void unblockUser (User admin, int id);
}
