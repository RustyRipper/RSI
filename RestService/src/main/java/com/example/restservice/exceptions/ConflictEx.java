package com.example.restservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictEx extends Exception{
    public ConflictEx(){
        super("CONFLICT!!!");
    }
}
