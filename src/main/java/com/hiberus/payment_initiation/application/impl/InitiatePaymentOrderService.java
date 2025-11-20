package com.hiberus.payment_initiation.application.impl;

import com.hiberus.payment_initiation.application.InitiatePaymentOrderUseCase;
import com.hiberus.payment_initiation.application.command.PaymentOrderInitiationCommand;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.spi.PaymentOrderLegacyClient;
import org.springframework.stereotype.Service;

/**
 * Implementation of the InitiatePaymentOrderUseCase.
 * <p>
 * This service orchestrates the initiation of a payment order by:
 * 1. Creating a domain entity from the command
 * 2. Delegating to the legacy SOAP client to initiate the payment order
 * 3. Returning the result from the legacy system
 */
@Service
public class InitiatePaymentOrderService implements InitiatePaymentOrderUseCase {

    private final PaymentOrderLegacyClient legacyClient;

    public InitiatePaymentOrderService(PaymentOrderLegacyClient legacyClient) {
        this.legacyClient = legacyClient;
    }

    @Override
    public PaymentOrder initiate(PaymentOrderInitiationCommand command) {
        // Create domain entity from command
        PaymentOrder paymentOrder = PaymentOrder.fromInitiationCommand(command);

        // Delegate to legacy client
        return legacyClient.initiatePaymentOrder(paymentOrder);
    }
}

