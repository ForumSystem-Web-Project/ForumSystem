package com.example.forumsystem.helpers;

import com.example.forumsystem.models.*;
import com.example.forumsystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommentMapper {

    private final CommentService commentService;

    @Autowired
    public CommentMapper(CommentService commentService) {
        this.commentService = commentService;
    }

    public Comment fromDtoForUpdate(int id, CommentDto commentDto) {

        Comment comment = commentService.getById(id);
        comment.setContent(commentDto.getContent());
        return comment;
    }

    public Comment fromDtoForCreation(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }

    public CommentDtoOut fromObjToDtoOut(Comment comment) {
        CommentDtoOut commentDtoOut = new CommentDtoOut();
        commentDtoOut.setContent(comment.getContent());
        commentDtoOut.setCreatedBy(comment.getCreatedBy().getUsername());
        return commentDtoOut;
    }

    public Comment fromDtoForUpdate(CommentDto commentDto, int commentID) {
        Comment commentDtoOut = commentService.getById(commentID);
        commentDtoOut.setContent(commentDto.getContent());
        return commentDtoOut;
    }

    public Comment fromDto(CommentDto dto, Post post, User user) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedBy(user);
        comment.setPost(post);
        return comment;
    }

    public CommentDtoOut toDtoOut(Comment comment) {
        CommentDtoOut commentDtoOut = new CommentDtoOut();
        commentDtoOut.setCommentId(comment.getCommentId());
        commentDtoOut.setCommentId(comment.getCommentId());
        commentDtoOut.setContent(comment.getContent());
        commentDtoOut.setCreatedBy(comment.getCreatedBy().getUsername());
        return commentDtoOut;
    }
}
