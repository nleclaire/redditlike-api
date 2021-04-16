package com.sei.redditlikeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InformationForbidden extends RuntimeException{
    public InformationForbidden(String message){
        super(message);
    }
}
