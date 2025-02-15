package com.codecool.solarwatch.model;

public class NoSuchCityException extends RuntimeException {
    public NoSuchCityException() {
        super("No such city found");
    }
}
