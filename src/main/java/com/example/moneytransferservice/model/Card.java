package com.example.moneytransferservice.model;

public record Card(String cardNumber, String cardValidTill, String cardCVV, Amount amount) {
}
