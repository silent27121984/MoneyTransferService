package com.example.moneytransferservice.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
public class TransferMoney {
    @NotEmpty
    @Length(min = 16)
    public String numberWriteOffCard;

    @NotEmpty
    @Length(min = 4)
    public String numberCardValidTill;

    @NotEmpty
    @Length(min = 3)
    public String numberCardCVV;

    @NotEmpty
    @Length(min = 16)
    public String numberCardForEnrolment;

    Amount amount;


    public String getNumberWriteOffCard() {
        return numberWriteOffCard;
    }

    public String getNumberCardForEnrolment() {
        return numberCardForEnrolment;
    }

    public Amount getAmount() {
        return amount;
    }

}