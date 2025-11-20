package com.hiberus.payment_initiation.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account reference DTO.
 * <p>
 * Represents an account reference with an IBAN (International Bank Account Number).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountReferenceDto {

    @NotBlank(message = "IBAN is required")
    private String iban;
}

