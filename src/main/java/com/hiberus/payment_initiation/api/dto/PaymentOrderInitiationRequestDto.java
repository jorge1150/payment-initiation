package com.hiberus.payment_initiation.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Payment order initiation request DTO.
 * <p>
 * Request body for initiating a new payment order. Equivalent to externalId in the legacy SOAP service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderInitiationRequestDto {

    @NotBlank(message = "External reference is required")
    private String externalReference;

    @NotNull(message = "Debtor account is required")
    @Valid
    private AccountReferenceDto debtorAccount;

    @NotNull(message = "Creditor account is required")
    @Valid
    private AccountReferenceDto creditorAccount;

    @NotNull(message = "Instructed amount is required")
    @Valid
    private AmountDto instructedAmount;

    private String remittanceInformation;

    @NotNull(message = "Requested execution date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestedExecutionDate;
}

