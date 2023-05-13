package com.example.restservice.controllers;

import com.example.restservice.exceptions.CarBadParams;
import com.example.restservice.exceptions.CarExistsEx;
import com.example.restservice.exceptions.CarNotFoundEx;
import com.example.restservice.exceptions.ConflictEx;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class FaultController {

    @ResponseBody
    @ExceptionHandler(CarNotFoundEx.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    ResponseEntity<?> CNFEHandler(CarNotFoundEx e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.NOT_FOUND)
                        .withTitle(HttpStatus.NOT_FOUND.name())
                        .withDetail(" - The car of ID DOES NOT EXIST"));

    }
    @ResponseBody
    @ExceptionHandler(CarExistsEx.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ResponseEntity<?> CEEHandler(CarExistsEx e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.BAD_REQUEST)
                        .withTitle(HttpStatus.BAD_REQUEST.name())
                        .withDetail(" - The car of ID EXIST"));
    }

    @ResponseBody
    @ExceptionHandler(ConflictEx.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<?> ConflictHandler(ConflictEx e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.CONFLICT)
                        .withTitle(HttpStatus.CONFLICT.name())
                        .withDetail(" - Cant register car, bad status"));
    }

    @ResponseBody
    @ExceptionHandler(CarBadParams.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<?> ParamsHandler(CarBadParams e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.BAD_REQUEST)
                        .withTitle(HttpStatus.BAD_REQUEST.name())
                        .withDetail(" - Bad params for car"));
    }

}
