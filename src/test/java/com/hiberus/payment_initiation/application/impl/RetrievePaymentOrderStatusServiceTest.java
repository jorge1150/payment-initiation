package com.hiberus.payment_initiation.application.impl;

import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import com.hiberus.payment_initiation.domain.spi.PaymentOrderLegacyClient;
import com.hiberus.payment_initiation.shared.exception.PaymentOrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrievePaymentOrderStatusServiceTest {

    @Mock
    private PaymentOrderLegacyClient legacyClient;

    @InjectMocks
    private RetrievePaymentOrderStatusService service;

    private String paymentOrderId;

    @BeforeEach
    void setUp() {
        paymentOrderId = "PO-12345678";
    }

    @Test
    void shouldReturnStatusWhenPaymentOrderExists() {
        // Given
        when(legacyClient.findStatus(paymentOrderId))
                .thenReturn(Optional.of(PaymentOrderStatus.EXECUTED));

        // When
        PaymentOrderStatus result = service.retrieveStatus(paymentOrderId);

        // Then
        assertThat(result).isEqualTo(PaymentOrderStatus.EXECUTED);

        // Verify that legacy client was called
        verify(legacyClient).findStatus(paymentOrderId);
    }

    @Test
    void shouldThrowNotFoundWhenStatusDoesNotExist() {
        // Given
        when(legacyClient.findStatus(paymentOrderId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> service.retrieveStatus(paymentOrderId))
                .isInstanceOf(PaymentOrderNotFoundException.class)
                .hasMessageContaining("Payment order not found with id: " + paymentOrderId);

        // Verify that legacy client was called
        verify(legacyClient).findStatus(paymentOrderId);
    }
}

