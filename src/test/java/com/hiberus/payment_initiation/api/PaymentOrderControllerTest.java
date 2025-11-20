package com.hiberus.payment_initiation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiberus.payment_initiation.api.dto.AccountReferenceDto;
import com.hiberus.payment_initiation.api.dto.AmountDto;
import com.hiberus.payment_initiation.api.dto.PaymentOrderInitiationRequestDto;
import com.hiberus.payment_initiation.api.error.GlobalExceptionHandler;
import com.hiberus.payment_initiation.application.InitiatePaymentOrderUseCase;
import com.hiberus.payment_initiation.application.RetrievePaymentOrderStatusUseCase;
import com.hiberus.payment_initiation.application.RetrievePaymentOrderUseCase;
import com.hiberus.payment_initiation.domain.Account;
import com.hiberus.payment_initiation.domain.Money;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import com.hiberus.payment_initiation.shared.exception.PaymentOrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentOrderController.class)
@Import(GlobalExceptionHandler.class)
class PaymentOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InitiatePaymentOrderUseCase initiatePaymentOrderUseCase;

    @MockBean
    private RetrievePaymentOrderUseCase retrievePaymentOrderUseCase;

    @MockBean
    private RetrievePaymentOrderStatusUseCase retrievePaymentOrderStatusUseCase;

    private PaymentOrderInitiationRequestDto validRequestDto;
    private PaymentOrder paymentOrder;

    @BeforeEach
    void setUp() {
        // Create valid request DTO matching Postman collection example
        validRequestDto = new PaymentOrderInitiationRequestDto();
        validRequestDto.setExternalReference("EXT-1");
        validRequestDto.setDebtorAccount(new AccountReferenceDto("EC12DEBTOR"));
        validRequestDto.setCreditorAccount(new AccountReferenceDto("EC98CREDITOR"));
        validRequestDto.setInstructedAmount(new AmountDto(150.75, "USD"));
        validRequestDto.setRemittanceInformation("Factura 001-123");
        validRequestDto.setRequestedExecutionDate(LocalDate.of(2025, 10, 31));

        // Create payment order response matching Postman collection example
        paymentOrder = new PaymentOrder();
        paymentOrder.setId("PO-0001");
        paymentOrder.setExternalReference("EXT-1");
        paymentOrder.setDebtorAccount(new Account("EC12DEBTOR"));
        paymentOrder.setCreditorAccount(new Account("EC98CREDITOR"));
        paymentOrder.setInstructedAmount(new Money(BigDecimal.valueOf(150.75), "USD"));
        paymentOrder.setRemittanceInformation("Factura 001-123");
        paymentOrder.setRequestedExecutionDate(LocalDate.of(2025, 10, 31));
        paymentOrder.setStatus(PaymentOrderStatus.PENDING_EXECUTION);
        paymentOrder.setCreationDateTime(OffsetDateTime.now());
        paymentOrder.setLastUpdateDateTime(OffsetDateTime.now());
    }

    @Test
    void shouldCreatePaymentOrderAndReturn201() throws Exception {
        // Given
        when(initiatePaymentOrderUseCase.initiate(any()))
                .thenReturn(paymentOrder);

        // When/Then
        mockMvc.perform(post("/payment-initiation/payment-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentOrderId").value("PO-0001"))
                .andExpect(jsonPath("$.status").value("PENDING_EXECUTION"))
                .andExpect(jsonPath("$.externalReference").value("EXT-1"));

        // Verify that use case was invoked
        verify(initiatePaymentOrderUseCase).initiate(any());
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        // Given - invalid request without required fields
        PaymentOrderInitiationRequestDto invalidRequest = new PaymentOrderInitiationRequestDto();
        // Missing externalReference, debtorAccount, etc.

        // When/Then
        mockMvc.perform(post("/payment-initiation/payment-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.startsWith("Request validation failed")))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").exists());
    }

    @Test
    void shouldReturn404WhenPaymentOrderNotFound() throws Exception {
        // Given
        String paymentOrderId = "PO-NOTFOUND";
        when(retrievePaymentOrderUseCase.retrieveById(paymentOrderId))
                .thenThrow(new PaymentOrderNotFoundException(paymentOrderId));

        // When/Then
        mockMvc.perform(get("/payment-initiation/payment-orders/{paymentOrderId}", paymentOrderId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PAYMENT_ORDER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Payment order not found with id: " + paymentOrderId))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").exists());
    }

    @Test
    void shouldReturnStatus200WhenStatusIsRetrieved() throws Exception {
        // Given - using paymentOrderId from Postman collection example
        String paymentOrderId = "PO-0001";
        when(retrievePaymentOrderStatusUseCase.retrieveStatus(paymentOrderId))
                .thenReturn(PaymentOrderStatus.EXECUTED);
        when(retrievePaymentOrderUseCase.retrieveById(paymentOrderId))
                .thenReturn(paymentOrder);

        // When/Then
        mockMvc.perform(get("/payment-initiation/payment-orders/{paymentOrderId}/status", paymentOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentOrderId").value(paymentOrderId))
                .andExpect(jsonPath("$.status").value("EXECUTED"))
                .andExpect(jsonPath("$.lastUpdateDateTime").exists());

        // Verify that use cases were invoked
        verify(retrievePaymentOrderStatusUseCase).retrieveStatus(paymentOrderId);
        verify(retrievePaymentOrderUseCase).retrieveById(paymentOrderId);
    }
}

