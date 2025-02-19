package com.example.forumsystem.controllers.mvc;

import com.example.forumsystem.exceptions.AuthenticationFailureException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.models.*;
import com.example.forumsystem.service.PostService;
import com.example.forumsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminMvcController {

    private final PostService postService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AdminMvcController(PostService postService, UserService userService, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "admin-login-page";
    }

    @PostMapping("/login")
    public String handleLogin(HttpSession session, Model model, @ModelAttribute("login") LoginDto loginDto) {
        try {
            authenticationHelper.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            session.setAttribute("currentUser", loginDto.getUsername());

            User user = userService.getByUsername(loginDto.getUsername());

            if (user == null || !user.isAdmin()) {
                model.addAttribute("error", "Access Denied! Only administrators can log in.");
                model.addAttribute("login", new LoginDto()); // Ensure login object is added back
                return "admin-login-page";
            }

            session.setAttribute("admin", user);
            return "redirect:/admin/dashboard";
        } catch (AuthenticationFailureException e) {
            model.addAttribute("error", "Invalid credentials.");
            model.addAttribute("login", new LoginDto());
            return "admin-login-page";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session){
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(@ModelAttribute("filterDto") FilterDto filterDto, @ModelAttribute("filterUserDto") FilterUserDto filterUserDto, HttpSession session, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }
        model.addAttribute("posts", postService.getAll(new FilterPostOptions(
                filterDto.getTitle(),
                null,
                null,
                filterDto.getSortBy(),
                filterDto.getSortOrder())));

        model.addAttribute("users", userService.getAll(new FilterUserOptions(
                filterUserDto.getFirstName(),
                null,
                filterUserDto.getUsername(),
                filterUserDto.getEmail(),
                filterUserDto.getSortBy(),
                filterUserDto.getSortOrder())));

        model.addAttribute("filterUserDto", new FilterUserDto());
        model.addAttribute("filterDto", new FilterDto());
        model.addAttribute("admin", admin);
        return "admin-page";
    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.blockUser(admin, id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.unblockUser(admin, id);
        return "redirect:/admin/users";
    }


    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
