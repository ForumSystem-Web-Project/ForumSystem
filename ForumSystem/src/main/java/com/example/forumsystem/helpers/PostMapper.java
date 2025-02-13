package com.example.forumsystem.helpers;

import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.PostDto;
import com.example.forumsystem.models.PostDtoOut;
import com.example.forumsystem.models.PostWithLikesAndCommentsDtoOut;
import com.example.forumsystem.service.CommentService;
import com.example.forumsystem.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public PostWithLikesAndCommentsDtoOut toDtoOutWithLikesAndComments(Post post) {
        PostWithLikesAndCommentsDtoOut postWithLikesAndCommentsDtoOut = new PostWithLikesAndCommentsDtoOut();
        postWithLikesAndCommentsDtoOut.setTitle(post.getTitle());
        postWithLikesAndCommentsDtoOut.setContent(post.getContent());
        postWithLikesAndCommentsDtoOut.setCreatedBy(post.getCreatedBy().getUsername());
        postWithLikesAndCommentsDtoOut.setCreatedAt(post.getCreatedAt());
        postWithLikesAndCommentsDtoOut.setLikes(post.getLikes().size());
        postWithLikesAndCommentsDtoOut.setComments(post.getComments());
        return postWithLikesAndCommentsDtoOut;
    }

    public List<PostWithLikesAndCommentsDtoOut> convertToDtoList(List<Post> posts) {
        return posts.stream()
                .map(post -> new PostWithLikesAndCommentsDtoOut(post.getTitle(), post.getContent(), post.getCreatedBy().getUsername(), post.getCreatedAt(), post.getLikes().size(), post.getComments()))
                .collect(Collectors.toList());
    }

    public List<PostDtoOut> listAllPostsDtoOut(List<Post> postList){
        List<PostDtoOut> listPostDtoOut = new ArrayList<>();
        for (Post post : postList){
            PostDtoOut postDtoOut = new PostDtoOut();
            postDtoOut.setTitle(post.getTitle());
            postDtoOut.setContent(post.getContent());
            postDtoOut.setCreatedBy(post.getCreatedBy().getUsername());
            listPostDtoOut.add(postDtoOut);
        }

        return listPostDtoOut;
    }
}
