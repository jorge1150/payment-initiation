package com.hiberus.payment_initiation.infrastructure.application;

import com.hiberus.payment_initiation.application.RetrievePaymentOrderStatusUseCase;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import org.springframework.stereotype.Service;

/**
 * Dummy implementation of RetrievePaymentOrderStatusUseCase.
 * <p>
 * This is a temporary implementation that throws UnsupportedOperationException.
 * It will be replaced with a real implementation that integrates with the SOAP service
 * and persistence layer in a future iteration.
 */
@Service
public class RetrievePaymentOrderStatusUseCaseAdapter implements RetrievePaymentOrderStatusUseCase {

    @Override
    public PaymentOrderStatus retrieveStatus(String paymentOrderId) {
        throw new UnsupportedOperationException(
                "RetrievePaymentOrderStatusUseCase not yet implemented. " +
                "This will be implemented with SOAP integration and persistence.");
    }
}

