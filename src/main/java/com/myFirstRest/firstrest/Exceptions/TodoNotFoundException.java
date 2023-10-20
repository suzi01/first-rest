package com.myFirstRest.firstrest.Exceptions;

public class TodoNotFoundException  extends RuntimeException{

    public TodoNotFoundException(String message){
        super(message);
    }
}
