package com.example.forumsystem.models;

import java.sql.Timestamp;
import java.util.List;

public class PostWithLikesAndCommentsDtoOut {

    private String title;

    private String content;

    private String createdBy;

    private Timestamp createdAt;

    private int likes;

    private List<Comment> comments;

    public PostWithLikesAndCommentsDtoOut() {
    }

    public PostWithLikesAndCommentsDtoOut(String title, String content, String createdBy, Timestamp createdAt, int likes, List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.likes = likes;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
