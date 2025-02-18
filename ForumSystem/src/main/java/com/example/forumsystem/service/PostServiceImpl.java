package com.example.forumsystem.service;

import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.helpers.PermissionHelpers;
import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.FilterPostOptions;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.CommentRepository;
import com.example.forumsystem.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Post> getAll(FilterPostOptions filterPostOptions) {
        return postRepository.getAll(filterPostOptions);
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
    public List<Post> getMostCommentedPosts() {
        return postRepository.getMostCommentedPosts();
    }

    @Override
    public List<Post> getMostRecentPosts() {
        return postRepository.getMostRecentPosts();
    }

    @Override
    public void createPost(Post post, User user) {

        PermissionHelpers.checkIfBlocked(user);
        post.setCreatedBy(user);
        postRepository.createPost(post);
    }

    @Override
    public void updatePost(Post post, User user) {

        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfCreatorOrAdminForPosts(user, post);
        Post existingPost = postRepository.getById(post.getId());
        if (post.getTitle().equals(existingPost.getTitle()) && post.getContent().equals(existingPost.getContent())) {
            throw new InvalidOperationException("No changes were made!");
        }
        postRepository.updatePost(post);
    }

    @Override
    public void deletePost(int id, User user) {

        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfCreatorOrAdminForPosts(user, postRepository.getById(id));
        postRepository.getById(id);
        postRepository.deletePost(id);
    }

    @Override
    public void addComment (int postId, User user, Comment comment) {

        PermissionHelpers.checkIfBlocked(user);

        Post post = postRepository.getById(postId);
        post.getComments().add(comment);
        comment.setPost(post);
        comment.setCreatedBy(user);
        commentRepository.create(comment);
        postRepository.updatePost(post);
    }

    @Override
    public void updateComment (int postId, User user, Comment comment) {

        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfCreatorOrAdmin(comment.getCreatedBy().getId(), user);

        Post post = postRepository.getById(postId);
        Comment oldComment = commentRepository.getById(comment.getCommentId());

        if (oldComment.getContent().equals(comment.getContent())) {
            throw new InvalidOperationException("Comment content is the same!");
        }
        commentRepository.update(comment);
        postRepository.updatePost(post);
    }

    @Override
    public void deleteComment (User user, Comment comment) {

        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfCreatorOrAdmin(comment.getCreatedBy().getId(), user);

        commentRepository.delete(comment.getCommentId());
    }

    @Override
    public void likePost (int postId, User user) {

        PermissionHelpers.checkIfBlocked(user);
        Post post = postRepository.getById(postId);

        if (post.getLikes().contains(user)){
            throw new InvalidOperationException("User has already liked this post!");
        }
        post.getLikes().add(user);
        postRepository.updatePost(post);
    }

    @Override
    public void unlikePost (int postId, User user) {

        PermissionHelpers.checkIfBlocked(user);
        Post post = postRepository.getById(postId);

        if (!post.getLikes().contains(user)){
            throw new InvalidOperationException("User hasn't liked this post!");
        }
        post.getLikes().remove(user);
        postRepository.updatePost(post);
    }
}
