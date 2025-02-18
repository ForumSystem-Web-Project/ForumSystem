package com.example.forumsystem.controllers.mvc;

import com.example.forumsystem.exeptions.AuthenticationFailureException;
import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.CommentMapper;
import com.example.forumsystem.helpers.PostMapper;
import com.example.forumsystem.models.*;
import com.example.forumsystem.service.CommentService;
import com.example.forumsystem.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/posts")
public class PostsMvcController {

    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public PostsMvcController(PostService postService, AuthenticationHelper authenticationHelper, PostMapper postMapper, CommentService commentService, CommentMapper commentMapper) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
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

    @GetMapping({"{id}/update"})
    public String showEditPostPage(@PathVariable int id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(id);
            PostDtoOut postDtoOut = postMapper.toDtoOut(post);
            model.addAttribute("postId", id);
            model.addAttribute("post", postDtoOut);
            return "update-post-page";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult errors,
                             HttpSession httpSession, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "update-post-page";
        }

        try {
            Post newPost = postMapper.fromDtoForUpdate(id, postDto);
            postService.updatePost(newPost, user);

            return "redirect:/posts";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "post.exists", e.getMessage());
            return "update-post-page";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
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

    @PostMapping("/{id}")
    public String addComment(@PathVariable int id,
                             @Valid @ModelAttribute("comment") CommentDto commentDto,
                             BindingResult errors,
                             HttpSession httpSession, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            model.addAttribute("error", "Invalid comment.");
            return "post-page";
        }

        try {
            Post post = postService.getById(id);
            Comment newComment = commentMapper.fromDto(commentDto, post, user);
            commentService.createComment(user, post, newComment);

            return "redirect:/posts/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "Post not found.");
            return "page-not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", "You are not authorized to comment.");
            return "access-denied";
        }
    }

    @GetMapping("/{postId}/comment/update/{commentId}")
    public String showEditCommentPage(@PathVariable int postId, @PathVariable int commentId, Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
            model.addAttribute("user", user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(postId);
            Comment comment = commentService.getById(commentId);

            if (!comment.getCreatedBy().equals(user)) {
                throw new UnauthorizedOperationException("You are not allowed to edit this comment.");
            }

            CommentDtoOut commentDtoOut = commentMapper.toDtoOut(comment);

            model.addAttribute("post", post);
            model.addAttribute("commentId", commentId);
            model.addAttribute("comment", commentDtoOut);

            return "update-comment-page";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{postId}/comment/update/{commentId}")
    public String updateComment(@PathVariable int postId, @PathVariable int commentId, @Valid @ModelAttribute("comment") CommentDto commentDto,
                             BindingResult errors,
                             HttpSession httpSession, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "update-comment-page";
        }

        try {
            Comment newComment = commentMapper.fromDtoForUpdate(commentId, commentDto);
            commentService.updateComment(user, newComment);

            return "redirect:/posts/" + postId;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "comment.exists", e.getMessage());
            return "update-comment-page";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

//    @PostMapping("/{id}/like")
//    public String likePost(@PathVariable int id,
//                           HttpSession httpSession, Model model) {
//
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(httpSession);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/login";
//        }
//
//        try {
//            postService.likePost(id, user);
//            return "redirect:/posts/" + id;
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", "Post not found.");
//            return "page-not-found";
//        } catch (UnauthorizedOperationException e) {
//            model.addAttribute("error", "You are not authorized to like this post.");
//            return "access-denied";
//        }
//    }
//
//    @PostMapping("/{id}/dislike")
//    public String dislikePost(@PathVariable int id,
//                              HttpSession httpSession, Model model) {
//
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(httpSession);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/login";
//        }
//
//        try {
//            postService.unlikePost(id, user);
//            return "redirect:/posts/" + id;
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", "Post not found.");
//            return "page-not-found";
//        } catch (UnauthorizedOperationException e) {
//            model.addAttribute("error", "You are not authorized to dislike this post.");
//            return "access-denied";
//        }
//    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
}
