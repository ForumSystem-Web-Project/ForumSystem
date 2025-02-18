package com.example.forumsystem.controllers.mvc;

import com.example.forumsystem.exeptions.AuthenticationFailureException;
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
    public String showAdminDashboard(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);
        return "admin-page";
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterDto") FilterDto filterDto, Model model) {
        FilterPostOptions filterPostOptions = new FilterPostOptions(
                filterDto.getTitle(),
                null,
                null,
                filterDto.getSortBy(),
                filterDto.getSortOrder()
        );
        model.addAttribute("posts", postService.getAll(filterPostOptions));
        return "admin-page";
    }

//    @GetMapping
//    public String showAllUsers(@ModelAttribute("filterUserDto") FilterUserDto filterUserDto, Model model) {
//        FilterUserOptions filterUserOptions = new FilterUserOptions(
//                filterUserDto.getFirstName(),
//                filterUserDto.getLastName(),
//                filterUserDto.getUsername(),
//                filterUserDto.getEmail(),
//                filterUserDto.getSortBy(),
//                filterUserDto.getSortOrder());
//        model.addAttribute("users", userService.getAll(filterUserOptions));
//        return "admin-page";
//    }


    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
