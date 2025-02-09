package com.example.forumsystem.helpers;

import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.PostDto;
import com.example.forumsystem.models.PostDtoOut;
import com.example.forumsystem.service.CommentService;
import com.example.forumsystem.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostMapper(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    public Post fromDtoForCreation(PostDto postDTO){
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        return post;
    }

    public Post fromDtoForUpdate(int id, PostDto postDto){
        Post post = postService.getById(id);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;
    }

    public PostDtoOut toDtoOut(Post post) {
        PostDtoOut postDtoOut = new PostDtoOut();
        postDtoOut.setTitle(post.getTitle());
        postDtoOut.setContent(post.getContent());
        postDtoOut.setCreatedBy(post.getCreatedBy().getUsername());
        postDtoOut.setCreatedAt(post.getCreatedAt());
        return postDtoOut;
    }
}
