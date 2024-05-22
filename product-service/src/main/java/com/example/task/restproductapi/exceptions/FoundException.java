package com.example.task.restproductapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FoundException extends RuntimeException {
    public FoundException(String message) {
        super(message);
    }
}
