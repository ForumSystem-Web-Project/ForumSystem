package com.example.forumsystem.helpers;

import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.PostDTO;
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

    public Post fromDto(PostDTO postDTO){
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        return post;
    }

    public Post fromDto(int id, PostDTO postDTO){
        Post post = fromDto(postDTO);
        post.setId(id);
        Post repositoryPost = postService.getById(id);
        post.setCreatedBy(repositoryPost.getCreatedBy());
        return post;
    }
}
