package com.hiberus.payment_initiation.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Payment order status resource DTO.
 * <p>
 * Minimal view used for status polling, containing only the payment order ID, status, and last update timestamp.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderStatusResourceDto {

    private String paymentOrderId;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime lastUpdateDateTime;
}

