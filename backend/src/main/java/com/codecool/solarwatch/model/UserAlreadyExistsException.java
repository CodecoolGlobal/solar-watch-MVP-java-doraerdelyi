package com.codecool.solarwatch.model;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("A user already exists with that email address");
    }
}
