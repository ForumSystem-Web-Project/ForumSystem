package com.example.forumsystem.controllers.mvc;

import com.example.forumsystem.exeptions.AuthenticationFailureException;
import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.PostMapper;
import com.example.forumsystem.models.FilterPostOptions;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.PostDto;
import com.example.forumsystem.models.User;
import com.example.forumsystem.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsMvcController {

    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;

    @Autowired
    public PostsMvcController(PostService postService, AuthenticationHelper authenticationHelper, PostMapper postMapper) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
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

    @GetMapping("/create")
    public String showNewPostPage(Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        model.addAttribute("post", new PostDto());
        return "create-post-page";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PostDto post, BindingResult errors, HttpSession httpSession) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "create-post-page";
        }

        try {
            Post newPost = postMapper.fromDto(post, user);
            postService.createPost(newPost, user);
            return "redirect:/posts";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "post.exists", e.getMessage());
            return "create-post-page";
        }
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        try {
            postService.deletePost(id, user);

            return "redirect:/posts";
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
}
