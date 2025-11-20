package com.hiberus.payment_initiation.application.impl;

import com.hiberus.payment_initiation.application.RetrievePaymentOrderUseCase;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.spi.PaymentOrderLegacyClient;
import com.hiberus.payment_initiation.shared.exception.PaymentOrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the RetrievePaymentOrderUseCase.
 * <p>
 * This service retrieves a payment order by its ID by delegating to the legacy SOAP client.
 * If the payment order is not found, it throws a PaymentOrderNotFoundException.
 */
@Service
public class RetrievePaymentOrderService implements RetrievePaymentOrderUseCase {

    private final PaymentOrderLegacyClient legacyClient;

    public RetrievePaymentOrderService(PaymentOrderLegacyClient legacyClient) {
        this.legacyClient = legacyClient;
    }

    @Override
    public PaymentOrder retrieveById(String paymentOrderId) {
        Optional<PaymentOrder> paymentOrder = legacyClient.findById(paymentOrderId);

        if (paymentOrder.isEmpty()) {
            throw new PaymentOrderNotFoundException(paymentOrderId);
        }

        return paymentOrder.get();
    }
}

