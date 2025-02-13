package com.example.forumsystem.controllers;

import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.exeptions.InvalidOperationException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.CommentMapper;
import com.example.forumsystem.helpers.PostMapper;
import com.example.forumsystem.models.*;
import com.example.forumsystem.service.CommentService;
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
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public PostRestController(PostService postService, PostMapper postMapper, AuthenticationHelper authenticationHelper,
                              CommentService commentService, CommentMapper commentMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    //Filtering and Sorting
    @GetMapping
    public List<PostDtoOut> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        FilterPostOptions filterOptions = new FilterPostOptions(title, content, createdBy,
                sortBy, sortOrder);

        return postMapper.listAllPostsDtoOut(postService.getAll(filterOptions));
    }

    @GetMapping("/{id}")
    public PostWithLikesAndCommentsDtoOut getPostById(@PathVariable int id) {
        try {
            Post post = postService.getById(id);
            return postMapper.toDtoOutWithLikesAndComments(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{title}")
    public PostWithLikesAndCommentsDtoOut getPostByTitle(@PathVariable String title) {
        try {
            Post post = postService.getByTitle(title);
            return postMapper.toDtoOutWithLikesAndComments(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/mostCommentedPosts")
    public List<PostWithLikesAndCommentsDtoOut> getMostCommentedPosts() {
        List<Post> posts = postService.getMostCommentedPosts();
        return postMapper.convertToDtoList(posts);
    }

    @GetMapping("/mostRecentPosts")
    public List<PostWithLikesAndCommentsDtoOut> getMostRecentPosts() {
        List<Post> posts = postService.getMostRecentPosts();
        return postMapper.convertToDtoList(posts);
    }

    @PostMapping
    public PostDtoOut createPost(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDtoForCreation(postDto);
            postService.createPost(post, user);
            return postMapper.toDtoOut(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public PostWithLikesAndCommentsDtoOut updatePost(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDtoForUpdate(id, postDto);
            postService.updatePost(post, user);
            return postMapper.toDtoOutWithLikesAndComments(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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

    @GetMapping("/comments/{commentId}")
    public CommentDtoOut getCommentById(@PathVariable int commentId) {
        try {
            Comment comment = commentService.getById(commentId);
            return commentMapper.fromObjToDtoOut(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/comments/{postId}")
    public CommentDtoOut addComment(@RequestHeader HttpHeaders headers, @PathVariable int postId, @Valid @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDtoForCreation(commentDto);
            postService.addComment(postId, user, comment);
            return commentMapper.fromObjToDtoOut(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/comments/{postId}")
    public CommentDtoOut updateComment(@RequestHeader HttpHeaders headers, @PathVariable int postId, @Valid @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDtoForCreation(commentDto);
            postService.updateComment(postId, user, comment);
            return commentMapper.fromObjToDtoOut(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping ("/comments/{postId}/{commentId}")
    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int postId, @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            postService.deleteComment(postId, user, commentService.getById(commentId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/likePost/{postId}")
    public PostWithLikesAndCommentsDtoOut likePost (@RequestHeader HttpHeaders headers, @PathVariable int postId){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getById(postId);
            postService.likePost(postId, user);
            return postMapper.toDtoOutWithLikesAndComments(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/unlikePost/{postId}")
    public PostWithLikesAndCommentsDtoOut unlikePost (@RequestHeader HttpHeaders headers, @PathVariable int postId){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getById(postId);
            postService.unlikePost(postId, user);
            return postMapper.toDtoOutWithLikesAndComments(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
