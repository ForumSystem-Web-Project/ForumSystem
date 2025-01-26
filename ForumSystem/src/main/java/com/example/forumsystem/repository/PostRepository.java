package com.example.forumsystem.repository;

import com.example.forumsystem.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> getAll();

    Post getById(int id);

    Post getByTitle(String title);

    void createPost(Post post);

    void updatePost(Post post);

    void deletePost(int id);
}
