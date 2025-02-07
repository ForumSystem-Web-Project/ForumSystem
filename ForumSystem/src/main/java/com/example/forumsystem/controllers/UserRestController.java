package com.example.forumsystem.controllers;

import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.UserMapper;
import com.example.forumsystem.models.User;
import com.example.forumsystem.models.UserCreateDto;
import com.example.forumsystem.models.UserDtoOut;
import com.example.forumsystem.models.UserUpdateDto;
import com.example.forumsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authorizationHelper;
    private final PhoneNumberService phoneNumberService;
    private final PhoneNumberMapper phoneNumberMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authorizationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }
    //Filtering and Sorting
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }
    //Done
    @GetMapping("/{id}")
    public User getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authorizationHelper.tryGetUser(headers);
            return userService.getById(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    //Done
    @GetMapping("/username/{username}")
    public User getUserByUsername(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            return userService.getByUsername(admin, username);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    //Done
    @GetMapping("/email/{email}")
    public User getUserByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            return userService.getByEmail(admin, email);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    //Done
    @GetMapping("/firstname/{firstname}")
    public User getUserByFirstName(@RequestHeader HttpHeaders headers, @PathVariable String firstname) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            return userService.getByFirstName(admin, firstname);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public UserDtoOut createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        try {
            User newUser = userMapper.createUserFromDto(userCreateDto);
            userService.createUser(newUser);
            return userMapper.createDtoOutToObjectForCreate(userCreateDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserDtoOut updateUser(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestBody UserUpdateDto userUpdateDto) {
        try {
            User modifier = authorizationHelper.tryGetUser(headers);
            User user = userMapper.createUpdatedUserFromDto(userUpdateDto, id);
            userService.updateUser(user, modifier);
            return userMapper.createDtoOutToObjectForUpdate(userUpdateDto, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
