package com.hiberus.payment_initiation.application;

/**
 * Exception thrown when a payment order is not found.
 * <p>
 * This exception is thrown by use cases when a requested payment order does not exist.
 * It will be caught by the API layer and mapped to an appropriate HTTP 404 response.
 */
public class PaymentOrderNotFoundException extends RuntimeException {

    public PaymentOrderNotFoundException(String message) {
        super(message);
    }

    public PaymentOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

