package com.hiberus.payment_initiation.application;

import com.hiberus.payment_initiation.domain.PaymentOrderStatus;

/**
 * Use case for retrieving the status of a payment order.
 * <p>
 * This port defines the contract for retrieving only the status of a payment order.
 * This is useful for status polling scenarios where only the status is needed.
 * The implementation will be provided by the infrastructure layer (adapter).
 */
public interface RetrievePaymentOrderStatusUseCase {

    /**
     * Retrieves the status of a payment order by its ID.
     *
     * @param paymentOrderId the payment order identifier
     * @return the payment order status
     * @throws PaymentOrderNotFoundException if the payment order is not found
     */
    PaymentOrderStatus retrieveStatus(String paymentOrderId);
}

