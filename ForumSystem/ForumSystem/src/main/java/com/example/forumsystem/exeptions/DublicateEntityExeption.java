package com.example.forumsystem.exeptions;

public class DublicateEntityExeption extends RuntimeException {

    public DublicateEntityExeption(String type, String attribute, String value){
        super(String.format("%s with %s %s already exists", type, attribute, value));
    }
}
