package com.example.forumsystem.service;

import com.example.forumsystem.exceptions.DuplicateEntityException;
import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.helpers.PermissionHelpers;
import com.example.forumsystem.models.FilterUserOptions;
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
    public List<User> getAll(FilterUserOptions filterUserOptions) {
        return userRepository.getAll(filterUserOptions);
    }

    @Override
    public User getById(User admin, int id) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        return userRepository.getById(id);
    }

    @Override
    public User getUserById(int id){
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByUsernameForAdmin(User admin, String username) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(User admin, String email) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByFirstName(User admin, String firstName) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        return userRepository.getByFirstName(firstName);
    }

    @Override
    public void createUser(User user) {

            boolean usernameExists = true;
            boolean emailExists = true;
            try {
                userRepository.getByUsername(user.getUsername());

            } catch (EntityNotFoundException e) {
                usernameExists = false;


            } try {
                userRepository.getByEmail(user.getEmail());

            } catch (EntityNotFoundException e) {
            emailExists = false;
            }


            if (usernameExists) {
                throw new DuplicateEntityException("User", "username", user.getUsername());

            } else if (emailExists){
                throw new DuplicateEntityException("User", "email", user.getEmail());
            }

            userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user, User modifier) {

        PermissionHelpers.checkIfBlocked(modifier);
        PermissionHelpers.checkIfCreatorOrAdmin(user.getId(), modifier);

        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByEmail(user.getEmail());
            if (user.getFirstName().equals(existingUser.getFirstName()) &&
            user.getLastName().equals(existingUser.getLastName()) &&
            user.getEmail().equals(existingUser.getEmail()) &&
            user.getPassword().equals(existingUser.getPassword())) {

                throw new InvalidOperationException("Invalid operation! Nothing was changed!");
            }

            if (existingUser.getId() == user.getId()){
                duplicateExists = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExists = false;

        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(User admin, int id) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        userRepository.deleteUser(id);
    }

    @Override
    public void makeAdmin (User admin, int id) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (user.isAdmin()){
            throw new InvalidOperationException("User is already admin!");
        }
        user.setAdmin(true);
        userRepository.updateUser(user);
    }

    @Override
    public void removeAdmin (User admin, int id) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (!user.isAdmin()){
            throw new InvalidOperationException("User is already an ordinary user!");
        }
        user.setAdmin(false);
        userRepository.updateUser(user);
    }

    @Override
    public void blockUser (User admin, int id) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (user.isBlocked()){
            throw new InvalidOperationException("User is already blocked!");
        }
        user.setBlocked(true);
        userRepository.updateUser(user);
    }

    @Override
    public void unblockUser (User admin, int id) {

        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (!user.isBlocked()){
            throw new InvalidOperationException("User is already not blocked!");
        }
        user.setBlocked(false);
        userRepository.updateUser(user);
    }
}
