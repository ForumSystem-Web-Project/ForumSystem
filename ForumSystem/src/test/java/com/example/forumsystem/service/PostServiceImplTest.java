package com.example.forumsystem.service;

import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.PermissionHelpers;
import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.FilterPostOptions;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.forumsystem.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void getAll_ShouldReturnAllPosts_WhenValid() {

        FilterPostOptions filterPostOptions = new FilterPostOptions("title", "content", "createdBy",
                "sortBy", "sortOrder");

        List<Post> allPosts = postService.getAll(filterPostOptions);

        Assertions.assertEquals(postService.getAll(filterPostOptions).size(), allPosts.size());
    }

    @Test
    public void getById_ShouldReturnPost_WhenValid() {

        Post mockPost = createMockPost();
        Mockito.when(postRepository.getById(1)).thenReturn(mockPost);

        Post result = postService.getById(1);

        Assertions.assertEquals(1, result.getId());
    }

    @Test
    public void getByTitle_ShouldReturnPost_WhenValid() {

        Post mockPost = createMockPost();
        Mockito.when(postRepository.getByTitle("Mock Title")).thenReturn(mockPost);


        Post result = postService.getByTitle("Mock Title");

        Assertions.assertEquals("Mock Title", result.getTitle());

    }

    @Test
    public void getMostCommentedPosts_ShouldReturnPosts_WhenValid() {

        Post mockPost = createMockPost();
        List<Post> mockMostCommentedPosts = new ArrayList<>();
        mockMostCommentedPosts.add(mockPost);
        when(postService.getMostCommentedPosts()).thenReturn(mockMostCommentedPosts);

        List<Post> mostCommentedPosts = postService.getMostCommentedPosts();

        Assertions.assertEquals(mockMostCommentedPosts, mostCommentedPosts);
    }

    @Test
    public void getMostRecentPosts_ShouldReturnPosts_WhenValid() {

        Post mockPost = createMockPost();
        List<Post> mockMostRecentPosts = new ArrayList<>();
        mockMostRecentPosts.add(mockPost);
        when(postService.getMostRecentPosts()).thenReturn(mockMostRecentPosts);

        List<Post> mostRecentPosts = postService.getMostRecentPosts();

        Assertions.assertEquals(mockMostRecentPosts, mostRecentPosts);
    }

    @Test
    public void create_ShouldThrowException_WhenUserIsBlocked() {

        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Post mockPost = createMockPost();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.createPost(mockPost, mockUser);
        });
    }

    @Test
    public void create_ShouldCreatePost_WhenValid() {

        User mockUser = createMockUser();
        mockUser.setBlocked(false);
        Post mockPost = createMockPost();

        Assertions.assertDoesNotThrow(() -> {
            postService.createPost(mockPost, mockUser);
        });
    }

    @Test
    public void update_ShouldThrowException_WhenUserIsBlocked() {

        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Post mockPost = createMockPost();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.updatePost(mockPost, mockUser);
        });
    }

    @Test
    public void update_ShouldThrowException_WhenUserIsNotAnAdmin() {

        User mockUser = createMockUser();
        mockUser.setBlocked(false);
        mockUser.setAdmin(false);
        Post mockPost = createMockPost();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.updatePost(mockPost, mockUser);
        });
    }

    @Test
    public void update_ShouldThrowException_WhenUserIsNotTheCreator() {

        User mockUser = createMockUser();
        User anotherMockUser = createAnotherMockUser();
        Post mockPost = createMockPost();
        mockPost.setCreatedBy(anotherMockUser);

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.updatePost(mockPost, mockUser);
        });
    }

    @Test
    public void update_ShouldThrowException_WhenPostContentsAreTheSame() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Post mockPost = createMockPost();
        Post anotherMockPost = createMockPost();

        when(postService.getById(mockPost.getId())).thenReturn(mockPost);

        Assertions.assertThrows(InvalidOperationException.class, () -> {postService.updatePost(anotherMockPost, mockUser);});
    }

    @Test
    public void update_ShouldUpdatePost_WhenValid() {

        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Post mockPost = createMockPost();
        Post anotherMockPost = createAnotherMockPost();
        anotherMockPost.setId(1);

        when(postService.getById(mockPost.getId())).thenReturn(mockPost);

        Assertions.assertDoesNotThrow(() -> {
            postService.updatePost(anotherMockPost, mockUser);
        });
    }

    @Test
    public void delete_ShouldThrowException_WhenUserIsBlocked() {

        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Post mockPost = createMockPost();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.deletePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void delete_ShouldThrowException_WhenUserIsNotAnAdmin() {

        User mockUser = createMockUser();
        mockUser.setAdmin(false);
        Post mockPost = createMockPost();

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.deletePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void delete_ShouldThrowException_WhenUserIsNotTheCreator() {

        User mockUser = createMockUser();
        User anotherMockUser = createAnotherMockUser();
        Post mockPost = createMockPost();
        mockPost.setCreatedBy(anotherMockUser);

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.deletePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void delete_ShouldDeletePost_WhenValid() {

        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Post mockPost = createMockPost();


        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        Assertions.assertDoesNotThrow(() -> {
            postService.deletePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void addComment_ShouldThrowException_WhenUserIsBlocked() {

        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.addComment(mockPost.getId(), mockUser, mockComment);
        });
    }

    @Test
    public void addComment_ShouldAddComment_WhenValid() {

        User mockUser = createMockUser();
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        Assertions.assertDoesNotThrow(() -> {
            postService.addComment(mockPost.getId(), mockUser, mockComment);
        });
    }

    @Test
    public void updateComment_ShouldThrowException_WhenUserIsBlocked() {

        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.updateComment(mockPost.getId(), mockUser, mockComment);
        });
    }

    @Test
    public void updateComment_ShouldThrowException_WhenUserIsNotAnAdmin() {

        User mockUser = createMockUser();
        mockUser.setAdmin(false);
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.updateComment(mockPost.getId(), mockUser, mockComment);
        });
    }

    @Test
    public void updateComment_ShouldThrowException_WhenUserIsNotCreator() {

        User mockUser = createMockUser();
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.updateComment(mockPost.getId(), mockUser, mockComment);
        });
    }

    @Test
    public void updateComment_ShouldThrowException_WhenCommentContentIsTheSame() {

        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();
        Comment mockComment2 = createMockComment();

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);
        when(commentRepository.getById(mockComment.getCommentId())).thenReturn(mockComment);

        Assertions.assertThrows(InvalidOperationException.class, () -> {
            postService.updateComment(mockPost.getId(), mockUser, mockComment2);
        });
    }

    @Test
    public void updateComment_ShouldUpdateComment_WhenValid() {

        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();
        Comment mockComment2 = createMockComment();

        mockComment2.setContent("Test Content 2");

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);
        when(commentRepository.getById(mockComment.getCommentId())).thenReturn(mockComment);

        Assertions.assertDoesNotThrow(() -> {
            postService.updateComment(mockPost.getId(), mockUser, mockComment2);
        });
    }

    @Test
    public void deleteComment_ShouldThrowException_WhenUserIsBlocked() {

        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.deleteComment(mockUser, mockComment);
        });
    }

    @Test
    public void deleteComment_ShouldThrowException_WhenUserIsNotTheCreator() {

        User mockUser = createMockUser();
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.deleteComment(mockUser, mockComment);
        });
    }

    @Test
    public void deleteComment_ShouldThrowException_WhenUserIsNotAnAdmin() {

        User mockUser = createMockUser();
        mockUser.setAdmin(false);
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.deleteComment(mockUser, mockComment);
        });
    }

    @Test
    public void deleteComment_ShouldThrowDeleteComment_WhenValid() {

        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Comment mockComment = createMockComment();

        assertDoesNotThrow(() -> {
            postService.deleteComment(mockUser, mockComment);
        });
    }

    @Test
    public void likePost_ShouldThrowException_WhenUserIsBlocked() {

        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.likePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void likePost_ShouldThrowException_WhenPostIsAlreadyLiked() {

        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockPost.getLikes().add(mockUser);

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        assertThrows(InvalidOperationException.class, () -> {
            postService.likePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void likePost_ShouldLikePost_WhenValid() {

        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        assertDoesNotThrow(() -> {
            postService.likePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void unlikePost_ShouldThrowException_WhenUserIsBlocked() {

        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        assertThrows(UnauthorizedOperationException.class, () -> {
            postService.unlikePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void unlikePost_ShouldThrowException_WhenPostIsAlreadyLiked() {

        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        assertThrows(InvalidOperationException.class, () -> {
            postService.unlikePost(mockPost.getId(), mockUser);
        });
    }

    @Test
    public void unlikePost_ShouldLikePost_WhenValid() {

        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockPost.getLikes().add(mockUser);

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        assertDoesNotThrow(() -> {
            postService.unlikePost(mockPost.getId(), mockUser);
        });
    }


}
