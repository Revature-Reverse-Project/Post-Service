package com.reverse.postservice.exceptions;

public class InvalidJwtException extends Exception{
    public InvalidJwtException(String message) {
        super(message);
    }
}
