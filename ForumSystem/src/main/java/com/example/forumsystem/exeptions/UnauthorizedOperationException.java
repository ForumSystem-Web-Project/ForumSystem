package com.example.forumsystem.exeptions;

public class UnauthorizedOperationException extends RuntimeException{

    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
