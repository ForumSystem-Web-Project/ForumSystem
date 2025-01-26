package com.example.forumsystem.service;

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
}
