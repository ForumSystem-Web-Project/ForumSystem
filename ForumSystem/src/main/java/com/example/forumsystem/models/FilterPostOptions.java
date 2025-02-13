package com.example.forumsystem.models;

import java.util.Optional;

public class FilterPostOptions {
    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> createdBy;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterPostOptions(
            String title,
            String content,
            String createdBy,
            String sortBy,
            String sortOrder   ) {

        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.createdBy = Optional.ofNullable(createdBy);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<String> getCreatedBy() {
        return createdBy;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
