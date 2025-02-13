package com.example.forumsystem.repository;

import com.example.forumsystem.models.FilterPostOptions;
import com.example.forumsystem.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> getAll(FilterPostOptions filterPostOptions);

    Post getById(int id);

    Post getByTitle(String title);

    List<Post> getMostCommentedPosts();

    List<Post> getMostRecentPosts();

    void createPost(Post post);

    void updatePost(Post post);

    void deletePost(int id);
}
