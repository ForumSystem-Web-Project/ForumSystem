package com.example.forumsystem;

import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Helpers {

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setFirstName("John");
        mockUser.setLastName("mockLastName");
        mockUser.setUsername("MockUsername");
        mockUser.setEmail("mock@user.com");
        mockUser.setPassword("password");
        mockUser.setAdmin(false);
        mockUser.setBlocked(false);
        return mockUser;
    }

    public static User createAnotherMockUser() {
        var mockUser = new User();
        mockUser.setId(3);
        mockUser.setFirstName("Jim");
        mockUser.setLastName("mockLastName2");
        mockUser.setUsername("MockUsername2");
        mockUser.setEmail("mock@user.com2");
        mockUser.setPassword("password2");
        mockUser.setAdmin(false);
        mockUser.setBlocked(false);
        return mockUser;
    }

    public static Post createMockPost() {
        Post mockPost = new Post();
        User user = createMockUser();
        user.setId(2);
        mockPost.setId(1);
        mockPost.setTitle("Mock Title");
        mockPost.setContent("Mock Content");
        mockPost.setCreatedBy(user);
        mockPost.setLikes(new HashSet<>());
        mockPost.setComments(new ArrayList<>());
        mockPost.setCreatedAt(Timestamp.from(Instant.now()));
        return mockPost;
    }

    public static Post createAnotherMockPost() {
        Post mockPost = new Post();
        User user = createMockUser();
        user.setId(2);
        mockPost.setId(2);
        mockPost.setTitle("Mock Title2");
        mockPost.setContent("Mock Content2");
        mockPost.setCreatedBy(user);
        mockPost.setLikes(new HashSet<>());
        mockPost.setComments(new ArrayList<>());
        mockPost.setCreatedAt(Timestamp.from(Instant.now()));
        return mockPost;
    }

    public static Comment createMockComment() {
        Comment mockComment = new Comment();
        User user = createMockUser();
        user.setId(2);
        mockComment.setCommentId(1);
        mockComment.setContent("Mock Content");
        mockComment.setCreatedBy(user);
        mockComment.setPost(createMockPost());
        return mockComment;
    }

    public static List<User> createMockUserList(){
        List<User> mockUserList = new ArrayList<>();
        mockUserList.add(createMockUser());
        User anotherUser = createMockUser();
        anotherUser.setId(2);
        anotherUser.setUsername("MockUsername2");
        mockUserList.add(anotherUser);

        return mockUserList;
    }

    public static PhoneNumber createMockPhoneNumber() {
        PhoneNumber mockPhoneNumber = new PhoneNumber();
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        mockPhoneNumber.setPhoneNumberId(1);
        mockPhoneNumber.setPhoneNumber("0888888888");
        mockPhoneNumber.setCreatedBy(mockUser);
        return mockPhoneNumber;
    }

    public static PhoneNumber createAnotherMockPhoneNumber() {
        PhoneNumber mockPhoneNumber = new PhoneNumber();
        mockPhoneNumber.setPhoneNumberId(1);
        mockPhoneNumber.setPhoneNumber("0888888888");
        return mockPhoneNumber;
    }
}
