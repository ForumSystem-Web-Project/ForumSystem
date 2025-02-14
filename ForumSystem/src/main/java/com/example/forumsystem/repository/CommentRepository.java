package com.example.forumsystem.repository;

import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAll();

    List<Comment> getByPost(Post post);

    List<Comment> getByUser(User user);

    Comment getById(int id);

    void create(Comment comment);

    void update(Comment comment);

    void delete(int id);
}

