package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.NoSuchCityException;
import com.codecool.solarwatch.model.NoSunriseSunsetDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class SolarWatchControllerAdvice {

    @ResponseBody
    @ExceptionHandler(NoSuchCityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidCityExceptionHandler(NoSuchCityException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NoSunriseSunsetDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String missingSunriseSunsetDataExceptionHandler(NoSunriseSunsetDataException ex) {
        return ex.getMessage();
    }
}


