package com.hiberus.payment_initiation.application.impl;

import com.hiberus.payment_initiation.application.command.PaymentOrderInitiationCommand;
import com.hiberus.payment_initiation.domain.Account;
import com.hiberus.payment_initiation.domain.Money;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import com.hiberus.payment_initiation.domain.spi.PaymentOrderLegacyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitiatePaymentOrderServiceTest {

    @Mock
    private PaymentOrderLegacyClient legacyClient;

    @InjectMocks
    private InitiatePaymentOrderService service;

    private PaymentOrderInitiationCommand command;
    private PaymentOrder expectedPaymentOrder;

    @BeforeEach
    void setUp() {
        // Create test command
        Account debtorAccount = new Account("ES1234567890123456789012");
        Account creditorAccount = new Account("ES9876543210987654321098");
        Money instructedAmount = new Money(BigDecimal.valueOf(150.75), "EUR");

        command = new PaymentOrderInitiationCommand(
                "EXT-001",
                debtorAccount,
                creditorAccount,
                instructedAmount,
                "Test payment",
                LocalDate.now().plusDays(1)
        );

        // Create expected payment order response from legacy client
        expectedPaymentOrder = new PaymentOrder();
        expectedPaymentOrder.setId("PO-12345678");
        expectedPaymentOrder.setExternalReference("EXT-001");
        expectedPaymentOrder.setDebtorAccount(debtorAccount);
        expectedPaymentOrder.setCreditorAccount(creditorAccount);
        expectedPaymentOrder.setInstructedAmount(instructedAmount);
        expectedPaymentOrder.setRemittanceInformation("Test payment");
        expectedPaymentOrder.setRequestedExecutionDate(LocalDate.now().plusDays(1));
        expectedPaymentOrder.setStatus(PaymentOrderStatus.PENDING_EXECUTION);
    }

    @Test
    void shouldInitiatePaymentOrderUsingLegacyClient() {
        // Given
        when(legacyClient.initiatePaymentOrder(any(PaymentOrder.class)))
                .thenReturn(expectedPaymentOrder);

        // When
        PaymentOrder result = service.initiate(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("PO-12345678");
        assertThat(result.getExternalReference()).isEqualTo("EXT-001");
        assertThat(result.getStatus()).isEqualTo(PaymentOrderStatus.PENDING_EXECUTION);

        // Verify that legacy client was called exactly once
        verify(legacyClient).initiatePaymentOrder(any(PaymentOrder.class));
    }
}

