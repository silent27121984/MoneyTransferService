package com.example.moneytransferservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.moneytransferservice.exception.ExceptionUnknownCard;
import com.example.moneytransferservice.exception.IncorrectDataEntry;
import com.example.moneytransferservice.exception.IncorrectCodeException;
import com.example.moneytransferservice.exception.NotEnoughMoneyException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(IncorrectDataEntry.class)
    public ResponseEntity<String> IncorrectDataHandler(IncorrectDataEntry e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExceptionUnknownCard.class)
    public ResponseEntity<String> UnknownCardHandler(ExceptionUnknownCard e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectCodeException.class)
    public ResponseEntity<String> IncorrectCodeHandler(IncorrectCodeException e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<String> enoughMoneyHandler(NotEnoughMoneyException e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
