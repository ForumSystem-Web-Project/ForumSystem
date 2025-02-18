package com.example.forumsystem.models;

public class CommentDtoOut {

    private int commentId;

    private String content;

    private String createdBy;

    public CommentDtoOut() {
    }

    public CommentDtoOut(String content, String createdBy) {
        this.content = content;
        this.createdBy = createdBy;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
