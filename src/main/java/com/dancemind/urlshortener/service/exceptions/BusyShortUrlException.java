package com.dancemind.urlshortener.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusyShortUrlException extends RuntimeException {

    public BusyShortUrlException() {
        super();
    }

    public BusyShortUrlException(String message) {
        super(message);
    }
}