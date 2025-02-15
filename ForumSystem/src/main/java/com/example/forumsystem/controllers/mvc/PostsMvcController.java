package com.example.forumsystem.controllers.mvc;

import com.example.forumsystem.models.FilterPostOptions;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsMvcController {

    private final PostService postService;

    @Autowired
    public PostsMvcController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String showAllPosts(Model model) {
        List<Post> posts = postService.getAll(
                new FilterPostOptions(
                        null,
                        null,
                        null,
                        null,
                        null));
        model.addAttribute("posts", posts);
        return "posts-page";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model) {
        try {
            Post post = postService.getById(id);
            model.addAttribute("post", post);
            return "post-page";
        } catch (Exception e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
