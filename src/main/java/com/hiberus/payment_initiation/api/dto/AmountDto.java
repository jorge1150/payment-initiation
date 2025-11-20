package com.hiberus.payment_initiation.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Amount DTO.
 * <p>
 * Represents a monetary amount with its currency.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Currency is required")
    private String currency;
}

