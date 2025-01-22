package com.example.forumsystem.exeptions;

public class EntityNotFoundExeption extends RuntimeException{

    public EntityNotFoundExeption(String type, int id) {
        this(type, "id", String.valueOf(id));
    }

    public EntityNotFoundExeption(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.", type, attribute, value));
    }
}
