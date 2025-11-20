package com.hiberus.payment_initiation.shared.exception;

/**
 * Exception thrown when a payment order is not found.
 * <p>
 * This exception is thrown by use cases when a requested payment order does not exist.
 * It will be caught by the API layer and mapped to an appropriate HTTP 404 response.
 */
public class PaymentOrderNotFoundException extends RuntimeException {

    public PaymentOrderNotFoundException(String paymentOrderId) {
        super("Payment order not found with id: " + paymentOrderId);
    }

    public PaymentOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

