package com.example.forumsystem.service;

import com.example.forumsystem.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAll();

    Comment getById(int id);
}
