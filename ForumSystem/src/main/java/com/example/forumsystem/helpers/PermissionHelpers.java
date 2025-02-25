package com.example.forumsystem.helpers;

import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.models.Post;
import com.example.forumsystem.models.User;

public class PermissionHelpers {

    private static final String AUTHORIZATION_PERMISSION_ERROR = "Invalid operation. Only an Admin or Creator can modify this entity!";
    private static final String ADMIN_AUTHORIZATION_ERROR = "Invalid operation. You are not an admin!";
    private static final String BLOCKED_USER_ERROR = "Invalid operation. Your profile has been blocked!";

    public static void checkIfAdmin(User user) {

        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(ADMIN_AUTHORIZATION_ERROR);
        }
    }

    public static void checkIfBlocked(User user) {

        if (user.isBlocked()) {
            throw new UnauthorizedOperationException(BLOCKED_USER_ERROR);
        }
    }

    public static void checkIfCreatorOrAdminForPosts(User user, Post post) {
        if (!user.equals(post.getCreatedBy()) && !user.isAdmin()) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }

    public static void checkIfCreatorOrAdmin(int userId, User user) {
        if (userId != user.getId() && !user.isAdmin()) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }
}