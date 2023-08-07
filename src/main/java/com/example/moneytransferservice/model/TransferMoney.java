package com.example.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TransferMoney(
        @NotBlank
        @Pattern(regexp = "\\d{16}")
        String numberWriteOffCard,
        @NotBlank
        @Pattern(regexp = "^(0[7-9]|1[0-2])/(2[3-9]|[3-9][0-9])$")
        String numberCardValidTill,
        @NotBlank
        @Pattern(regexp = "\\d{3}")
        String numberCardCVV,
        @NotBlank
        @Pattern(regexp = "\\d{16}")
        String numberCardForEnrolment,
        @NotNull
        Amount amount) {

}
