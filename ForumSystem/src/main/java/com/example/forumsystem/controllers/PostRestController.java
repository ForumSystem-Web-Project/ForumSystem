package com.example.forumsystem.controllers;

import com.example.forumsystem.exeptions.DublicateEntityExeption;
import com.example.forumsystem.exeptions.EntityNotFoundExeption;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.PostDTO;
import com.example.forumsystem.models.User;
import com.example.forumsystem.service.PostService;
import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.getAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        try{
            return postService.getById(id);
        } catch (EntityNotFoundExeption ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{title}")
    public Post getUserByTitle(@PathVariable String title) {
        try {
            return postService.getByTitle(title);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
