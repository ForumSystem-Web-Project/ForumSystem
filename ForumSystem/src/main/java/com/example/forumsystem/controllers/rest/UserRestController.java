package com.example.forumsystem.controllers.rest;

import com.example.forumsystem.exceptions.DuplicateEntityException;
import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.PhoneNumberMapper;
import com.example.forumsystem.helpers.UserMapper;
import com.example.forumsystem.models.*;
import com.example.forumsystem.service.PhoneNumberService;
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
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper,
                              PhoneNumberService phoneNumberService, PhoneNumberMapper phoneNumberMapper) {
        this.userService = userService;
        this.authorizationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.phoneNumberService = phoneNumberService;
        this.phoneNumberMapper = phoneNumberMapper;
    }

    @GetMapping
    public List<UserDtoOut> getAll(@RequestParam(required = false) String firstName,
                                   @RequestParam(required = false) String lastName,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String sortBy,
                                   @RequestParam(required = false) String orderBy) {
        FilterUserOptions filterOptions = new FilterUserOptions(firstName, lastName,
                username, email, sortBy, orderBy);
        return userMapper.allUsersToDtoOut(userService.getAll(filterOptions));
    }

    @GetMapping("/{id}")
    public UserDtoOut getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getById(admin, id);
            return userMapper.createDtoOut(existingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public UserDtoOut getUserByUsername(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getByUsernameForAdmin(admin, username);
            return userMapper.createDtoOut(existingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public UserDtoOut getUserByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getByEmail(admin, email);
            return userMapper.createDtoOut(existingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/firstname/{firstname}")
    public UserDtoOut getUserByFirstName(@RequestHeader HttpHeaders headers, @PathVariable String firstname) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getByFirstName(admin, firstname);
            return userMapper.createDtoOut(existingUser);
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
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } 
    }

    @PutMapping("/{id}")
    public UserDtoOut updateUser(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
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
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            userService.deleteUser(admin, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/makeAdmin/{id}")
    public void makeUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            userService.makeAdmin(admin, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/removeAdmin/{id}")
    public void removeUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            userService.removeAdmin(admin, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/blockUser/{id}")
    public void blockUser (@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            userService.blockUser(admin, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/unblockUser/{id}")
    public void unblockUser (@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            userService.unblockUser(admin, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/phoneNumber")
    public PhoneNumber getAdminPhoneNumber(@RequestHeader HttpHeaders headers) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            return phoneNumberService.getByUserId(admin);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/phoneNumber")
    public PhoneNumber createPhone(@RequestHeader HttpHeaders headers, @Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            PhoneNumber phoneNumber = phoneNumberMapper.map(phoneNumberDto);
            phoneNumberService.createPhoneNumber(admin, phoneNumber);
            return phoneNumber;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/phoneNumber")
    public PhoneNumber updatePhone(@RequestHeader HttpHeaders headers, @Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            PhoneNumber phoneNumber = phoneNumberMapper.updateMap(phoneNumberDto, admin);
            phoneNumberService.updatePhoneNumber(admin, phoneNumber);
            return phoneNumber;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/phoneNumber")
    public void deletePhone(@RequestHeader HttpHeaders headers){
        try {
            User admin = authorizationHelper.tryGetUser(headers);
            phoneNumberService.deletePhoneNumber(admin);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
