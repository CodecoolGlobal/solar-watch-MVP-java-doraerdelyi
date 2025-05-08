package com.codecool.solarwatch.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("A user already exists with that email address");
    }
}
