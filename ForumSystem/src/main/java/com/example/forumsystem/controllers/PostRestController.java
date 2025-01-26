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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;

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

//    @PostMapping
//    public Post createPost(@Valid @RequestBody PostDTO postDTO) {
//        try {
//            Post post = modelMapper.fromDto(postDTO);
//            postService.createPost(post);
//            return post;
//        } catch (EntityNotFoundExeption e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (DublicateEntityExeption e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//        } catch (UnknownError e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public Post updatePost(@PathVariable int id, @Valid @RequestBody PostDTO postDTO) {
//        try {
//            Post post = modelMapper.fromDto(postDTO, id);
//            postService.updatePost(post, user);
//            return post;
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (EntityNotFoundExeption e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//        } catch (UnauthorizedOperationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public void deletePost(@PathVariable int id) {
//        try {
//            postService.deletePost(id, user);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (UnauthorizedOperationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
}
