package com.example.moneytransferservice.exception;

public class IncorrectDataEntry extends RuntimeException {
    public IncorrectDataEntry(String s) {
        super(s);
    }
}
