package com.example.restservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CarNotFoundEx extends Exception {
    public CarNotFoundEx() {
        super("The specified car does not exist");
    }
}