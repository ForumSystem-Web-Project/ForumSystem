package com.example.forumsystem.service;

import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;

import java.util.List;

public interface CommentService {

    List<Comment> getAll();

    Comment getById(int id);

    List<Comment> getByPost(Post post);

    List<Comment> getByUser(User user);

    void createComment(User user, Post post, Comment comment);

    void updateComment (User user, Comment comment);

    void deleteComment (User user, int id);
}
