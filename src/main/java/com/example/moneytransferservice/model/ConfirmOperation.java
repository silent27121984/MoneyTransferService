package com.example.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ConfirmOperation(
        @NotBlank
        @Pattern(regexp = "\\d{3}")
        String operationId,
        @NotNull
        String code){
}
