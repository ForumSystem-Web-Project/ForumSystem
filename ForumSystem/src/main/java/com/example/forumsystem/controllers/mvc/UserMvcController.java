package com.example.forumsystem.controllers.mvc;

import com.example.forumsystem.exceptions.AuthenticationFailureException;
import com.example.forumsystem.exceptions.DuplicateEntityException;
import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.UserMapper;
import com.example.forumsystem.models.*;
import com.example.forumsystem.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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


    @GetMapping("/{id}/update")
    public String showUpdateUserPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);

            if (loggedInUser.getId() != id && !loggedInUser.isAdmin()) {
                throw new UnauthorizedOperationException("You can only update your own account.");
            }

            User user = userService.getUserById(id);
            UserUpdateDto userUpdateDto = userMapper.toUpdateDto(user);

            model.addAttribute("userId", id);
            model.addAttribute("user", userUpdateDto);

            return "update-user-page";

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id,
                             @Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
                             BindingResult errors,
                             HttpSession session,
                             Model model) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);

            if (loggedInUser.getId() != id && !loggedInUser.isAdmin()) {
                throw new UnauthorizedOperationException("You can only update your own account.");
            }


            User updatedUser = userMapper.createUpdatedUserFromDto(userUpdateDto, id);
            userService.updateUser(updatedUser, loggedInUser);

            return "redirect:/users";

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "user.exists", e.getMessage());
            return "update-user-page";
        } catch (EntityNotFoundException e) {
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

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
}
