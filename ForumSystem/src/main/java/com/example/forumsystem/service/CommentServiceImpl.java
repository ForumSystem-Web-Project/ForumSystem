package com.example.forumsystem.service;

import com.example.forumsystem.exeptions.InvalidOperationException;
import com.example.forumsystem.helpers.PermissionHelpers;
import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Override
    public Comment getById(int id) {
        return commentRepository.getById(id);
    }

    @Override
    public List<Comment> getByPost(Post post) {
        return commentRepository.getByPost(post);
    }

    @Override
    public List<Comment> getByUser(User user) {
        return commentRepository.getByUser(user);
    }

    @Override
    public void createComment(User user, Post post, Comment comment) {
        PermissionHelpers.checkIfBlocked(user);
        comment.setPost(post);
        comment.setCreatedBy(user);
        commentRepository.create(comment);
    }

    @Override
    public void updateComment (User user, Comment comment) {
        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfCreatorOrAdmin(comment.getCreatedBy().getId(), user);
        Comment existingComment = commentRepository.getById(comment.getCommentId());
        if (existingComment.getContent().equals(comment.getContent())){
            throw new InvalidOperationException("Invalid operation! Updated comment content is the same!");
        }
        commentRepository.update(comment);
    }

    @Override
    public void deleteComment (User user, int id) {
        Comment comment = commentRepository.getById(id);

        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfCreatorOrAdmin(comment.getCreatedBy().getId(), user);
        commentRepository.delete(comment);
    }
}
