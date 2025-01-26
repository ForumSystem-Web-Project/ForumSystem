package com.example.forumsystem.service;

import com.example.forumsystem.exeptions.DublicateEntityExeption;
import com.example.forumsystem.exeptions.EntityNotFoundExeption;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final String MODIFY_POST_ERROR_MESSAGE = "Only admin or post creator can modify a post.";
    private final PostRepository postRepository;

    @Autowired
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
        boolean duplicateExists = true;
        try {
            postRepository.getByTitle(post.getTitle());
        } catch (EntityNotFoundExeption e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityNotFoundExeption("Post", "title", post.getTitle());
        }

        post.setCreatedBy(user);
        postRepository.createPost(post);
    }

    @Override
    public void updatePost(Post post, User user) {
        checkModifyPermissions(post.getId(), user);

        boolean duplicateExists = true;
        try {
            Post existingPost = postRepository.getByTitle(post.getTitle());
            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundExeption e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DublicateEntityExeption("Post", "title", post.getTitle());
        }

        postRepository.updatePost(post);
    }

    @Override
    public void deletePost(int id, User user) {
        checkModifyPermissions(id, user);
        postRepository.deletePost(id);
    }

    private void checkModifyPermissions(int postId, User user) {
        Post post = postRepository.getById(postId);
        if (post.getCreatedBy().equals(user)) {
            throw new UnauthorizedOperationException(MODIFY_POST_ERROR_MESSAGE);
        }
    }
}
