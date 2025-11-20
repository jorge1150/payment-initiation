package com.hiberus.payment_initiation.infrastructure.soap;

import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import com.hiberus.payment_initiation.domain.spi.PaymentOrderLegacyClient;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementation of PaymentOrderLegacyClient that simulates integration with the legacy SOAP service.
 * <p>
 * This is a stub implementation that simulates the behavior of the legacy PaymentOrderService SOAP service.
 * In a real environment, this class would use a SOAP client (Spring Web Services, JAX-WS, etc.) to
 * communicate with the actual legacy service using the WSDL contract.
 * <p>
 * TODO: Replace this stub with actual SOAP client implementation:
 * - Configure SOAP client using Spring Web Services or JAX-WS
 * - Map domain objects to SOAP request/response DTOs
 * - Handle SOAP faults and exceptions
 * - Implement retry logic and error handling
 */
@Component
public class LegacyPaymentOrderSoapClient implements PaymentOrderLegacyClient {

    @Override
    public PaymentOrder initiatePaymentOrder(PaymentOrder paymentOrder) {
        // TODO: Implement actual SOAP call to PaymentOrderService.initiatePaymentOrder
        // This would involve:
        // 1. Mapping PaymentOrder domain entity to SOAP request DTO
        // 2. Calling the SOAP service endpoint
        // 3. Mapping SOAP response DTO back to PaymentOrder domain entity
        // 4. Handling SOAP faults and exceptions

        // Simulate SOAP call delay
        try {
            Thread.sleep(50); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Create a new PaymentOrder based on the input, with ID and status from legacy system
        PaymentOrder result = new PaymentOrder();
        result.setId(paymentOrder.getId() != null ? paymentOrder.getId() : generatePaymentOrderId());
        result.setExternalReference(paymentOrder.getExternalReference());
        result.setDebtorAccount(paymentOrder.getDebtorAccount());
        result.setCreditorAccount(paymentOrder.getCreditorAccount());
        result.setInstructedAmount(paymentOrder.getInstructedAmount());
        result.setRemittanceInformation(paymentOrder.getRemittanceInformation());
        result.setRequestedExecutionDate(paymentOrder.getRequestedExecutionDate());
        result.setStatus(PaymentOrderStatus.PENDING_EXECUTION); // Legacy system sets status to PENDING_EXECUTION
        result.setCreationDateTime(OffsetDateTime.now());
        result.setLastUpdateDateTime(OffsetDateTime.now());

        return result;
    }

    @Override
    public Optional<PaymentOrder> findById(String paymentOrderId) {
        // TODO: Implement actual SOAP call to PaymentOrderService.findById
        // This would involve:
        // 1. Creating SOAP request with paymentOrderId
        // 2. Calling the SOAP service endpoint
        // 3. Mapping SOAP response DTO to PaymentOrder domain entity
        // 4. Handling "not found" scenarios (SOAP faults or empty responses)

        // Simulate SOAP call delay
        try {
            Thread.sleep(30); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate "not found" scenario: if ID starts with "NOT_FOUND", return empty
        if (paymentOrderId != null && paymentOrderId.startsWith("NOT_FOUND")) {
            return Optional.empty();
        }

        // Simulate finding a payment order
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId(paymentOrderId);
        paymentOrder.setExternalReference("EXT-" + paymentOrderId);
        paymentOrder.setDebtorAccount(new com.hiberus.payment_initiation.domain.Account("ES1234567890123456789012"));
        paymentOrder.setCreditorAccount(new com.hiberus.payment_initiation.domain.Account("ES9876543210987654321098"));
        paymentOrder.setInstructedAmount(new com.hiberus.payment_initiation.domain.Money(
                java.math.BigDecimal.valueOf(150.75),
                "EUR"
        ));
        paymentOrder.setRemittanceInformation("Simulated payment order");
        paymentOrder.setRequestedExecutionDate(java.time.LocalDate.now().plusDays(1));
        paymentOrder.setStatus(PaymentOrderStatus.EXECUTED); // Simulate different statuses
        paymentOrder.setCreationDateTime(OffsetDateTime.now().minusHours(2));
        paymentOrder.setLastUpdateDateTime(OffsetDateTime.now().minusMinutes(30));

        return Optional.of(paymentOrder);
    }

    @Override
    public Optional<PaymentOrderStatus> findStatus(String paymentOrderId) {
        // TODO: Implement actual SOAP call to PaymentOrderService.findStatus
        // This would involve:
        // 1. Creating SOAP request with paymentOrderId
        // 2. Calling the SOAP service endpoint
        // 3. Mapping SOAP response to PaymentOrderStatus
        // 4. Handling "not found" scenarios

        // Simulate SOAP call delay
        try {
            Thread.sleep(20); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate "not found" scenario
        if (paymentOrderId != null && paymentOrderId.startsWith("NOT_FOUND")) {
            return Optional.empty();
        }

        // Simulate status retrieval - for simplicity, return EXECUTED
        // In a real scenario, this would query the legacy system for the current status
        return Optional.of(PaymentOrderStatus.EXECUTED);
    }

    /**
     * Generates a payment order ID in the format expected by the legacy system.
     * <p>
     * In a real implementation, the ID would be generated by the legacy SOAP service.
     *
     * @return a generated payment order ID
     */
    private String generatePaymentOrderId() {
        return "PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

