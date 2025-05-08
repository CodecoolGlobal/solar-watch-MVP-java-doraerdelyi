package com.codecool.solarwatch.exception;

public class NoSuchCityException extends RuntimeException {
    public NoSuchCityException() {
        super("No such city found");
    }
}
