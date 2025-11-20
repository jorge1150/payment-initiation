package com.hiberus.payment_initiation.infrastructure.application;

import com.hiberus.payment_initiation.application.RetrievePaymentOrderUseCase;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import org.springframework.stereotype.Service;

/**
 * Dummy implementation of RetrievePaymentOrderUseCase.
 * <p>
 * This is a temporary implementation that throws UnsupportedOperationException.
 * It will be replaced with a real implementation that integrates with the SOAP service
 * and persistence layer in a future iteration.
 */
@Service
public class RetrievePaymentOrderUseCaseAdapter implements RetrievePaymentOrderUseCase {

    @Override
    public PaymentOrder retrieveById(String paymentOrderId) {
        throw new UnsupportedOperationException(
                "RetrievePaymentOrderUseCase not yet implemented. " +
                "This will be implemented with SOAP integration and persistence.");
    }
}

