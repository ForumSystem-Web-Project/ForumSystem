package com.example.forumsystem.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {


    @NotNull
    @Size(min = 1, max = 2000, message = "Comment should be between 1 and 2000 symbols!")
    private String content;

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }
}
