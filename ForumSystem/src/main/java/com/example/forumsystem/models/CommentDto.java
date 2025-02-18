package com.example.forumsystem.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotNull
    private int commentId;

    @NotNull
    @Size(min = 1, max = 2000, message = "Comment should be between 1 and 2000 symbols!")
    private String content;

    public CommentDto() {
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }
}
