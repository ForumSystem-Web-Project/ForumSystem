package com.example.forumsystem.repository;

import com.example.forumsystem.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAll();

    Comment getById(int id);
}

