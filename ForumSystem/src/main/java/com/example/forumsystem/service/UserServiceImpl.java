package com.example.forumsystem.service;

import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByName(String username) {
        return userRepository.getByName(username);
    }

    @Override
    public void createUser(User user) {
            boolean duplicateExists = true;
            try {
                userRepository.getByName(user.getUsername());
            } catch (EntityNotFoundException e) {
                duplicateExists = false;
            }

            if (duplicateExists) {
                throw new DuplicateEntityException("User", "username", user.getUsername());
            }

            userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user) {
    //Creator or Admin check

        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        //Creator or Admin check

        userRepository.deleteUser(id);
    }
}
