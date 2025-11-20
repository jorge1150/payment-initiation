package com.hiberus.payment_initiation.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Payment order resource DTO.
 * <p>
 * Full representation of a PaymentOrder returned by the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderResourceDto {

    private String paymentOrderId;

    private String status;

    private String externalReference;

    private AccountReferenceDto debtorAccount;

    private AccountReferenceDto creditorAccount;

    private AmountDto instructedAmount;

    private String remittanceInformation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestedExecutionDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime creationDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime lastUpdateDateTime;
}

