package com.example.forumsystem.service;

import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;

import java.util.List;

public interface PostService {

    List<Post> getAll();

    Post getById(int id);

    Post getByTitle(String title);

    void createPost(Post post, User user);

    void updatePost(Post post, User user);

    void deletePost(int id, User user);

    void addComment (int postId, User user, Comment comment);

    void updateComment (int postId, User user, Comment comment);

    void deleteComment (int postId, User user, Comment comment);

    void likePost (int postId, User user);

    void unlikePost (int postId, User user);
}
