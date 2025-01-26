package com.example.forumsystem.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PostDTO {

    @NotNull(message = "Title name can't be empty")
    @Size(min = 16, max = 64, message = "Title should be between 16 and 64 symbols")
    private String title;

    @NotNull(message = "Content name can't be empty")
    @Size(min = 32, max = 8192, message = "Content should be between 2 and 20 symbols")
    private String content;

    @Positive(message = "Likes must be positive")
    private int likes;

    public PostDTO() {
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
