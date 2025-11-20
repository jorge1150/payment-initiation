package com.hiberus.payment_initiation.domain.spi;

import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;

import java.util.Optional;

/**
 * Port (SPI - Service Provider Interface) for the legacy SOAP PaymentOrderService.
 * <p>
 * This interface defines the contract for communicating with the legacy SOAP service
 * PaymentOrderService. It is part of the domain layer (SPI) and will be implemented
 * by adapters in the infrastructure layer.
 * <p>
 * This port follows the Dependency Inversion Principle: the domain defines what it needs,
 * and the infrastructure provides the implementation.
 */
public interface PaymentOrderLegacyClient {

    /**
     * Initiates a payment order in the legacy system.
     *
     * @param paymentOrder the payment order to initiate
     * @return the payment order with its assigned ID and updated status from the legacy system
     */
    PaymentOrder initiatePaymentOrder(PaymentOrder paymentOrder);

    /**
     * Finds a payment order by its ID in the legacy system.
     *
     * @param paymentOrderId the payment order identifier
     * @return an Optional containing the payment order if found, empty otherwise
     */
    Optional<PaymentOrder> findById(String paymentOrderId);

    /**
     * Finds the status of a payment order by its ID in the legacy system.
     *
     * @param paymentOrderId the payment order identifier
     * @return an Optional containing the payment order status if found, empty otherwise
     */
    Optional<PaymentOrderStatus> findStatus(String paymentOrderId);
}

