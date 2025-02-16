package com.example.forumsystem.controllers.mvc;

import com.example.forumsystem.exeptions.AuthenticationFailureException;
import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.UserMapper;
import com.example.forumsystem.models.*;
import com.example.forumsystem.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        List<User> users = userService.getAll(
                new FilterUserOptions(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null));
        model.addAttribute("users", users);
        return "users-page";
    }

    @GetMapping({"{id}/update"})
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        try {
            User user1 = userMapper.toDto(id);
            model.addAttribute("userId", id);
            model.addAttribute("user", user1);
            return "update-user-page";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id, @Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
                             BindingResult errors,
                             HttpSession httpSession, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "update-user-page";
        }

        try {
            User newUser = userMapper.createUpdatedUserFromDto(userUpdateDto, id);
            userService.updateUser(newUser, user);

            return "redirect:/users";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "user.exists", e.getMessage());
            return "update-user-page";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
