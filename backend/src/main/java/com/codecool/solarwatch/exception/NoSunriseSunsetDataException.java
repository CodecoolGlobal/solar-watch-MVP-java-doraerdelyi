package com.codecool.solarwatch.exception;

public class NoSunriseSunsetDataException extends RuntimeException {
    public NoSunriseSunsetDataException() {
        super("There was an error fetching sunrise and sunset data");
    }
}
