package com.example.forumsystem.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorMvcController {

    @RequestMapping("/not-found")
    public String handleError() {
        return "page-not-found";
    }

    @RequestMapping("/access-denied")
    public String handleError2() {
        return "access-denied";
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
