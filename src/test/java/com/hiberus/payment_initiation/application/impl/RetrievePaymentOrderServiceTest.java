package com.hiberus.payment_initiation.application.impl;

import com.hiberus.payment_initiation.domain.Account;
import com.hiberus.payment_initiation.domain.Money;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import com.hiberus.payment_initiation.domain.spi.PaymentOrderLegacyClient;
import com.hiberus.payment_initiation.shared.exception.PaymentOrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrievePaymentOrderServiceTest {

    @Mock
    private PaymentOrderLegacyClient legacyClient;

    @InjectMocks
    private RetrievePaymentOrderService service;

    private String paymentOrderId;
    private PaymentOrder paymentOrder;

    @BeforeEach
    void setUp() {
        paymentOrderId = "PO-12345678";

        paymentOrder = new PaymentOrder();
        paymentOrder.setId(paymentOrderId);
        paymentOrder.setExternalReference("EXT-001");
        paymentOrder.setDebtorAccount(new Account("ES1234567890123456789012"));
        paymentOrder.setCreditorAccount(new Account("ES9876543210987654321098"));
        paymentOrder.setInstructedAmount(new Money(BigDecimal.valueOf(150.75), "EUR"));
        paymentOrder.setRemittanceInformation("Test payment");
        paymentOrder.setRequestedExecutionDate(LocalDate.now().plusDays(1));
        paymentOrder.setStatus(PaymentOrderStatus.EXECUTED);
        paymentOrder.setCreationDateTime(OffsetDateTime.now().minusHours(2));
        paymentOrder.setLastUpdateDateTime(OffsetDateTime.now().minusMinutes(30));
    }

    @Test
    void shouldReturnPaymentOrderWhenExists() {
        // Given
        when(legacyClient.findById(paymentOrderId))
                .thenReturn(Optional.of(paymentOrder));

        // When
        PaymentOrder result = service.retrieveById(paymentOrderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(paymentOrderId);
        assertThat(result.getExternalReference()).isEqualTo("EXT-001");
        assertThat(result.getStatus()).isEqualTo(PaymentOrderStatus.EXECUTED);

        // Verify that legacy client was called
        verify(legacyClient).findById(paymentOrderId);
    }

    @Test
    void shouldThrowNotFoundWhenPaymentOrderDoesNotExist() {
        // Given
        when(legacyClient.findById(paymentOrderId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> service.retrieveById(paymentOrderId))
                .isInstanceOf(PaymentOrderNotFoundException.class)
                .hasMessageContaining("Payment order not found with id: " + paymentOrderId);

        // Verify that legacy client was called
        verify(legacyClient).findById(paymentOrderId);
    }
}

