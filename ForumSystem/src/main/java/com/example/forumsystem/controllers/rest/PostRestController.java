package com.example.forumsystem.controllers.rest;

import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.AuthenticationHelper;
import com.example.forumsystem.helpers.CommentMapper;
import com.example.forumsystem.helpers.PostMapper;
import com.example.forumsystem.models.*;
import com.example.forumsystem.service.CommentService;
import com.example.forumsystem.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Controller", description = "Different post related options. Create, update, delete, like, unlike or comment a post. Multiple choices.")
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

    @Operation(summary = "Get all posts.", description = "Get all posts in the app with filtering provided.")
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

    @Operation(summary = "Get a specific post.", description = "Get a post by providing its ID.")
    @GetMapping("/postId/{id}")
    public PostWithLikesAndCommentsDtoOut getPostById(@PathVariable int id) {
        try {
            Post post = postService.getById(id);
            return postMapper.toDtoOutWithLikesAndComments(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get a specific post.", description = "Get a post by providing its title.")
    @GetMapping("/title/{title}")
    public PostWithLikesAndCommentsDtoOut getPostByTitle(@PathVariable String title) {
        try {
            Post post = postService.getByTitle(title);
            return postMapper.toDtoOutWithLikesAndComments(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get the 10 most commented posts.", description = "Get the 10 most commented posts in the app at the time.")
    @GetMapping("/mostCommentedPosts")
    public List<PostWithLikesAndCommentsDtoOut> getMostCommentedPosts() {
        List<Post> posts = postService.getMostCommentedPosts();
        return postMapper.convertToDtoList(posts);
    }

    @Operation(summary = "Get the 10 most recent posts.", description = "Get the 10 most recent posts in the app at the time.")
    @GetMapping("/mostRecentPosts")
    public List<PostWithLikesAndCommentsDtoOut> getMostRecentPosts() {
        List<Post> posts = postService.getMostRecentPosts();
        return postMapper.convertToDtoList(posts);
    }

    @Operation(summary = "Create a post.", description = "Create a post.")
    @PostMapping
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Update a post.", description = "Update a post.")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Delete a post.", description = "Delete a post.")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Get a comment.", description = "Get a specific comment by its ID.")
    @GetMapping("/comments/{commentId}")
    public CommentDtoOut getCommentById(@PathVariable int commentId) {
        try {
            Comment comment = commentService.getById(commentId);
            return commentMapper.fromObjToDtoOut(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Comment a post.", description = "Add a comment to a specific post.")
    @PostMapping("/comments/{postId}")
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Update a comment.", description = "Update a comment from a specific post.")
    @PutMapping("/comments/{postId}/{commentId}")
    @SecurityRequirement(name = "authHeader")
    public CommentDtoOut updateComment(@RequestHeader HttpHeaders headers, @PathVariable int postId, @PathVariable int commentId, @Valid @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDtoForUpdate(commentDto, commentId);
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

    @Operation(summary = "Delete a comment.", description = "Delete a comment from a specific post.")
    @DeleteMapping ("/comments/{commentId}")
    @SecurityRequirement(name = "authHeader")
    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            postService.deleteComment(user, commentService.getById(commentId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Like post.", description = "Like a specific post.")
    @PutMapping("/likePost/{postId}")
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Unlike post.", description = "Unlike a specific post.")
    @PutMapping("/unlikePost/{postId}")
    @SecurityRequirement(name = "authHeader")
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
