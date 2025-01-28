package com.example.forumsystem.controllers;

import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.PostMapper;
import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostRestController(PostService postService, PostMapper postMapper, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        try {
            return postService.getById(id);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
    public Post createPost(@RequestHeader HttpHeaders headers @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDto(postDto);
            postService.createPost(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Post updatePost(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDto(id, postDto);
            postService.updatePost(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deletePost(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            postService.deletePost(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("{postId}/comments")
    public Post addComment(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestBody Comment comment) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getById(id);
            postService.addComment(id, comment);
            return postDtoOut;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("{postId}/likes")
    public Post likePost (@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getById(id);
            postService.likePost(id);
            return postDtoOut;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("{postId}/likes")
    public Post unlikePost (@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getById(id);
            postService.unlikePost(id);
            return postDtoOut;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
