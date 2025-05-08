package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.exception.NoSuchCityException;
import com.codecool.solarwatch.exception.NoSunriseSunsetDataException;
import com.codecool.solarwatch.exception.UserAlreadyExistsException;
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
    public String handleInvalidCity(NoSuchCityException ex) {return ex.getMessage();}

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

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ex.getMessage();
    }
}


