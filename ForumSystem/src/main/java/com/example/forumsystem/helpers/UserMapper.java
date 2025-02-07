package com.example.forumsystem.helpers;

import com.example.forumsystem.models.User;
import com.example.forumsystem.models.UserCreateDto;

import com.example.forumsystem.models.UserDtoOut;
import com.example.forumsystem.models.UserUpdateDto;
import com.example.forumsystem.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //creation
    private User dtoToObject(UserCreateDto userCreateDto) {
        User user = new User();
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());
        user.setAdmin(false);
        user.setBlocked(false);
        return user;
    }
    //creation
    public User createUserFromDto(UserCreateDto userCreateDto) {
        return dtoToObject(userCreateDto);
    }
    //creation
    public UserDtoOut createDtoOutToObjectForCreate(UserCreateDto userCreateDto) {
        UserDtoOut userOut = new UserDtoOut();
        userOut.setFirstName(userCreateDto.getFirstName());
        userOut.setLastName(userCreateDto.getLastName());
        userOut.setUsername(userCreateDto.getUsername());
        userOut.setEmail(userCreateDto.getEmail());
        return userOut;
    }

    //updating
    public User createUpdatedUserFromDto(UserUpdateDto userUpdateDto, int id) {
        User existingUser = userRepository.getById(id); // Fetch from DB
        existingUser.setFirstName(userUpdateDto.getFirstName());
        existingUser.setLastName(userUpdateDto.getLastName());
        existingUser.setPassword(userUpdateDto.getPassword());
        return existingUser;
    }
    //updating
    public UserDtoOut createDtoOutToObjectForUpdate(UserUpdateDto userUpdateDto, int id) {
        UserDtoOut userOut = new UserDtoOut();
        userOut.setFirstName(userUpdateDto.getFirstName());
        userOut.setLastName(userUpdateDto.getLastName());
        userOut.setUsername(userRepository.getById(id).getUsername());
        userOut.setEmail(userUpdateDto.getEmail());
        return userOut;
    }
}
