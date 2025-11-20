package com.hiberus.payment_initiation.application.impl;

import com.hiberus.payment_initiation.application.RetrievePaymentOrderStatusUseCase;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import com.hiberus.payment_initiation.domain.spi.PaymentOrderLegacyClient;
import com.hiberus.payment_initiation.shared.exception.PaymentOrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the RetrievePaymentOrderStatusUseCase.
 * <p>
 * This service retrieves only the status of a payment order by its ID by delegating
 * to the legacy SOAP client. If the payment order is not found, it throws a
 * PaymentOrderNotFoundException.
 */
@Service
public class RetrievePaymentOrderStatusService implements RetrievePaymentOrderStatusUseCase {

    private final PaymentOrderLegacyClient legacyClient;

    public RetrievePaymentOrderStatusService(PaymentOrderLegacyClient legacyClient) {
        this.legacyClient = legacyClient;
    }

    @Override
    public PaymentOrderStatus retrieveStatus(String paymentOrderId) {
        Optional<PaymentOrderStatus> status = legacyClient.findStatus(paymentOrderId);

        if (status.isEmpty()) {
            throw new PaymentOrderNotFoundException(paymentOrderId);
        }

        return status.get();
    }
}

