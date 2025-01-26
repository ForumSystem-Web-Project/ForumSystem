package com.example.forumsystem.helpers;

import com.example.forumsystem.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final PostService postService;

    @Autowired
    public ModelMapper(PostService postService) {
        this.postService = postService;

    }
}
