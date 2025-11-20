package com.hiberus.payment_initiation.infrastructure.application;

import com.hiberus.payment_initiation.application.InitiatePaymentOrderUseCase;
import com.hiberus.payment_initiation.application.command.PaymentOrderInitiationCommand;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import org.springframework.stereotype.Service;

/**
 * Dummy implementation of InitiatePaymentOrderUseCase.
 * <p>
 * This is a temporary implementation that throws UnsupportedOperationException.
 * It will be replaced with a real implementation that integrates with the SOAP service
 * and persistence layer in a future iteration.
 */
@Service
public class InitiatePaymentOrderUseCaseAdapter implements InitiatePaymentOrderUseCase {

    @Override
    public PaymentOrder initiate(PaymentOrderInitiationCommand command) {
        throw new UnsupportedOperationException(
                "InitiatePaymentOrderUseCase not yet implemented. " +
                "This will be implemented with SOAP integration and persistence.");
    }
}

