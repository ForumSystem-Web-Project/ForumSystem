package com.example.forumsystem.service;

import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAll() {
        return postRepository.getAll();
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public Post getByTitle(String title) {
        return postRepository.getByTitle(title);
    }

    @Override
    public void createPost(Post post, User user) {

    }

    @Override
    public void updatePost(Post post, User user) {

    }

    @Override
    public void deletePost(int id, User user) {

    }
}
