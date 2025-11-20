package com.hiberus.payment_initiation.application;

import com.hiberus.payment_initiation.application.command.PaymentOrderInitiationCommand;
import com.hiberus.payment_initiation.domain.PaymentOrder;

/**
 * Use case for initiating a new payment order.
 * <p>
 * This port defines the contract for initiating a payment order. The implementation
 * will be provided by the infrastructure layer (adapter).
 */
public interface InitiatePaymentOrderUseCase {

    /**
     * Initiates a new payment order based on the provided command.
     *
     * @param command the payment order initiation command
     * @return the created payment order with its assigned ID
     */
    PaymentOrder initiate(PaymentOrderInitiationCommand command);
}

