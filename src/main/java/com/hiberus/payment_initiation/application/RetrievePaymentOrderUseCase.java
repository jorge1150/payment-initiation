package com.hiberus.payment_initiation.application;

import com.hiberus.payment_initiation.domain.PaymentOrder;

/**
 * Use case for retrieving a payment order by its ID.
 * <p>
 * This port defines the contract for retrieving a payment order. The implementation
 * will be provided by the infrastructure layer (adapter).
 */
public interface RetrievePaymentOrderUseCase {

    /**
     * Retrieves a payment order by its ID.
     *
     * @param paymentOrderId the payment order identifier
     * @return the payment order
     * @throws PaymentOrderNotFoundException if the payment order is not found
     */
    PaymentOrder retrieveById(String paymentOrderId);
}

