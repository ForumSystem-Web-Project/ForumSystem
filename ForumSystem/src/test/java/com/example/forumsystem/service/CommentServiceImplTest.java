package com.example.forumsystem.service;

import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.forumsystem.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    public void getAll_ShouldReturnAllComments_WhenValid() {
        List<Comment> mockComments = List.of(createMockComment(), createMockComment());

        when(commentRepository.getAll()).thenReturn(mockComments);

        List<Comment> result = commentService.getAll();

        assertEquals(mockComments, result);
    }

    @Test
    public void getById_ShouldReturnComment_WhenValid() {
        Comment mockComment = createMockComment();

        when(commentRepository.getById(mockComment.getCommentId())).thenReturn(mockComment);

        Comment result = commentService.getById(mockComment.getCommentId());

        assertEquals(commentService.getById(mockComment.getCommentId()), result);
    }

    @Test
    public void getByPost_ShouldReturnAllComments_WhenValid() {
        List<Comment> mockComments = List.of(createMockComment(), createMockComment());
        Post mockPost = createMockPost();

        when(commentRepository.getByPost(mockPost)).thenReturn(mockComments);

        List<Comment> result = commentService.getByPost(mockPost);

        assertEquals(mockComments, result);
    }

    @Test
    public void getByUser_ShouldReturnAllComments_WhenValid() {
        List<Comment> mockComments = List.of(createMockComment(), createMockComment());
        User mockUser = createMockUser();

        when(commentRepository.getByUser(mockUser)).thenReturn(mockComments);

        List<Comment> result = commentService.getByUser(mockUser);

        assertEquals(mockComments, result);
    }

    @Test
    public void createComment_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Comment mockComment = createMockComment();
        Post mockPost = createMockPost();

        assertThrows(UnauthorizedOperationException.class, () -> {
            commentService.createComment(mockUser, mockPost, mockComment);
        });
    }

    @Test
    public void createComment_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();
        Comment mockComment = createMockComment();
        Post mockPost = createMockPost();

        assertDoesNotThrow(() -> {
            commentService.createComment(mockUser, mockPost, mockComment);
        });
    }

    @Test
    public void updateComment_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            commentService.updateComment(mockUser, mockComment);
        });
    }

    @Test
    public void updateComment_ShouldThrowException_WhenUserIsNotACreatorOrAdmin() {
        User mockUser = createAnotherMockUser();
        Comment mockComment = createMockComment();

        assertThrows(UnauthorizedOperationException.class, () -> {
            commentService.updateComment(mockUser, mockComment);
        });
    }

    @Test
    public void updateComment_ShouldThrowException_WhenCommentIsUnchanged() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Comment oldMockComment = createMockComment();
        Comment newMockComment = createMockComment();

        when(commentRepository.getById(oldMockComment.getCommentId())).thenReturn(oldMockComment);

        assertThrows(InvalidOperationException.class, () -> {
            commentService.updateComment(mockUser, newMockComment);
        });
    }

    @Test
    public void updateComment_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Comment oldMockComment = createMockComment();
        Comment newMockComment = createMockComment();
        newMockComment.setContent("Mock Content 2");

        when(commentRepository.getById(oldMockComment.getCommentId())).thenReturn(oldMockComment);

        assertDoesNotThrow(() -> {
            commentService.updateComment(mockUser, newMockComment);
        });
    }

    @Test
    public void deleteComment_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Comment mockComment = createMockComment();

        when(commentRepository.getById(mockComment.getCommentId())).thenReturn(mockComment);

        assertThrows(UnauthorizedOperationException.class, () -> {
            commentService.deleteComment(mockUser, mockComment.getCommentId());
        });
    }

    @Test
    public void deleteComment_ShouldThrowException_WhenUserIsNotACreatorOrAdmin() {
        User mockUser = createMockUser();
        Comment mockComment = createMockComment();

        when(commentRepository.getById(mockComment.getCommentId())).thenReturn(mockComment);

        assertThrows(UnauthorizedOperationException.class, () -> {
            commentService.deleteComment(mockUser, mockComment.getCommentId());
        });
    }

    @Test
    public void deleteComment_ShouldCallRepo_WhenValid() {
        Comment mockComment = createMockComment();

        when(commentRepository.getById(mockComment.getCommentId())).thenReturn(mockComment);

        assertDoesNotThrow(() -> {
            commentService.deleteComment(mockComment.getCreatedBy(), mockComment.getCommentId());
        });
    }

}
