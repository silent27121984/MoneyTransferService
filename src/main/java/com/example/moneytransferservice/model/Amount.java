package com.example.moneytransferservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Setter
public record Amount(@Min(0) Integer value, @NotBlank String currency){
}
