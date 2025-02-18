package com.example.forumsystem.exeptions;

public class AuthenticationFailureException extends RuntimeException{

    public AuthenticationFailureException(String message) {
        super(message);
    }
}
