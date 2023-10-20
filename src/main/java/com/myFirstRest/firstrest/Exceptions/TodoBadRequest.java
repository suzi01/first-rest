package com.myFirstRest.firstrest.Exceptions;

public class TodoBadRequest extends RuntimeException {

    public TodoBadRequest(String message){
        super(message);
    }
}
