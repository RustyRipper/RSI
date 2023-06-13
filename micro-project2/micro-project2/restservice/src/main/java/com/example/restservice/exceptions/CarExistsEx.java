package com.example.restservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CarExistsEx extends Exception{
    public CarExistsEx(){
        super("This car already exists");
    }
}
