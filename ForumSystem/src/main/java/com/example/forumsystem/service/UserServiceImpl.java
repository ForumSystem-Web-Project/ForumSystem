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
    public User getById(User modifier, int id) {
        //Creator or Admin check
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(User modifier, String username) {
        //Creator or Admin check
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(User modifier, String email) {
        //Creator or Admin check
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByFirstName(User modifier, String firstName) {
        //Creator or Admin check
        return userRepository.getByFirstName(firstName);
    }

    @Override
    public void createUser(User user) {
        //Creator or Admin check
            boolean duplicateExists = true;
            boolean duplicateExists2 = true;
            try {
                userRepository.getByUsername(user.getUsername());

            } catch (EntityNotFoundException e) {
                duplicateExists = false;


            } try {
                userRepository.getByEmail(user.getEmail());

            } catch (EntityNotFoundException e) {
            duplicateExists2 = false;
            }


            if (duplicateExists) {
                throw new DuplicateEntityException("User", "username", user.getUsername());

            } else if (duplicateExists2){
                throw new DuplicateEntityException("User", "email", user.getEmail());
            }

            userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user, User modifier, int id) {
    //Creator or Admin check
        // Needs an updateUserDTO to avoid updating the username
        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(User modifier, int id) {
        //Creator or Admin check

        userRepository.deleteUser(id);
    }

    @Override
    public void blockUser (User admin, int id) {
        //Admin check


    }
}
