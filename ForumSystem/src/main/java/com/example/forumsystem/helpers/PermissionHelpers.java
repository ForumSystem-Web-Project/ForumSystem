package com.example.forumsystem.helpers;

import com.example.forumsystem.models.User;

public class PermissionHelpers {

    private static final String AUTHORIZATION_PERMISSION_ERROR = "Invalid operation. Only an Admin or Post Creator can modify the Post!";
    private static final String ADMIN_AUTHORIZATION_ERROR = "Invalid operation. You are not an admin!";
    private static final String BLOCKED_USER_ERROR = "Invalid operation. Your profile has been blocked!";
    private static final String ANONYMOUS_AUTHORIZATION_ERROR = "Invalid operation. You are not logged in!";


    public static void adminCheck (User user) {



    }
}
