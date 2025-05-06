package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.NoSuchCityException;
import com.codecool.solarwatch.model.NoSunriseSunsetDataException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SolarWatchControllerAdvice {

    @ResponseBody
    @ExceptionHandler(NoSuchCityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidCity(NoSuchCityException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NoSunriseSunsetDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingSunriseSunsetData(NoSunriseSunsetDataException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCredentials(BadCredentialsException ex) {
        return ex.getMessage();
    }
}


