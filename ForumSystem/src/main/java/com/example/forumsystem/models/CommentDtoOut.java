package com.example.forumsystem.models;

public class CommentDtoOut {

    private String content;

    private String createdBy;

    public CommentDtoOut() {
    }

    public CommentDtoOut(String content, String createdBy) {
        this.content = content;
        this.createdBy = createdBy;
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
