package com.challenge.vasconcelos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String defaultExceptionHandler(Exception ex) {
        return "Internal Server Error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class })
    public String invalidExceptionHandler(Exception ex) {
        return "Bad Request: " + ex.getMessage();
    }

}
