package com.hiberus.payment_initiation.domain;

/**
 * Payment order status enumeration.
 * <p>
 * Status of the PaymentOrder. Values inspired by BIAN and underlying legacy service.
 */
public enum PaymentOrderStatus {
    INITIATED,
    PENDING_EXECUTION,
    EXECUTED,
    REJECTED
}

