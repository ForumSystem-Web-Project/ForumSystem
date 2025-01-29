package com.example.forumsystem.service;

import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        post = new Post();
        post.setId(1);
        post.setTitle("Test Post");
        post.setCreatedBy(user);
    }

    @Test
    void testGetAll() {
        when(postRepository.getAll())
                .thenReturn(List.of(post));

        List<Post> posts = postService.getAll();

        assertEquals(1, posts.size());
        verify(postRepository, times(1)).getAll();
    }

    @Test
    void testGetById() {
        when(postRepository.getById(1))
                .thenReturn(post);

        Post foundPost = postService.getById(1);

        assertEquals(post, foundPost);
        verify(postRepository, times(1)).getById(1);
    }

    @Test
    void testGetByTitle() {
        when(postRepository.getByTitle("Test Post"))
                .thenReturn(post);

        Post foundPost = postService.getByTitle("Test Post");

        assertEquals(post, foundPost);
        verify(postRepository, times(1)).getByTitle("Test Post");
    }

    @Test
    void testCreatePost_Success() {
        when(postRepository.getByTitle("Test Post"))
                .thenThrow(new EntityNotFoundException("Post", "title", "Test Post"));

        doNothing().when(postRepository).createPost(post);

        assertDoesNotThrow(() -> postService.createPost(post, user));

        verify(postRepository, times(1)).createPost(post);
    }

    @Test
    void testCreatePost_DuplicateEntityException() {
        when(postRepository.getByTitle("Test Post"))
                .thenReturn(post);

        assertThrows(EntityNotFoundException.class, () -> postService.createPost(post, user));
    }

    @Test
    void testUpdatePost_Success() {
        when(postRepository.getById(1))
                .thenReturn(post);

        when(postRepository.getByTitle("Test Post"))
                .thenThrow(new EntityNotFoundException("Post", "title", "Test Post"));

        doNothing().when(postRepository).updatePost(post);

        assertDoesNotThrow(() -> postService.updatePost(post, user));
        verify(postRepository, times(1)).updatePost(post);
    }

    @Test
    void testUpdatePost_DuplicateEntityException() {
        when(postRepository.getById(1))
                .thenReturn(post);

        when(postRepository.getByTitle("Test Post"))
                .thenReturn(post);

        assertThrows(DuplicateEntityException.class, () -> postService.updatePost(post, user));
    }

    @Test
    void testDeletePost_Success() {
        when(postRepository.getById(1))
                .thenReturn(post);

        doNothing().when(postRepository).deletePost(1);

        assertDoesNotThrow(() -> postService.deletePost(1, user));
        verify(postRepository, times(1)).deletePost(1);
    }

    @Test
    void testDeletePost_UnauthorizedOperationException() {
        User otherUser = new User();
        otherUser.setId(2);
        when(postRepository.getById(1))
                .thenReturn(post);

        assertThrows(UnauthorizedOperationException.class, () -> postService.deletePost(1, otherUser));
    }
}
