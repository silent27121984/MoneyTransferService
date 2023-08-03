package com.example.moneytransferservice.exception;

public class IncorrectCodeException extends RuntimeException {
    public IncorrectCodeException(String s) {
        super(s);
    }
}
