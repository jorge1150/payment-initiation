package com.hiberus.payment_initiation.api;

import com.hiberus.payment_initiation.api.dto.PaymentOrderInitiationRequestDto;
import com.hiberus.payment_initiation.api.dto.PaymentOrderResourceDto;
import com.hiberus.payment_initiation.api.dto.PaymentOrderStatusResourceDto;
import com.hiberus.payment_initiation.api.mapper.PaymentOrderMapper;
import com.hiberus.payment_initiation.application.InitiatePaymentOrderUseCase;
import com.hiberus.payment_initiation.application.RetrievePaymentOrderStatusUseCase;
import com.hiberus.payment_initiation.application.RetrievePaymentOrderUseCase;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Payment Order operations.
 * <p>
 * This controller exposes the Payment Order API endpoints as defined in the OpenAPI contract.
 * It delegates business logic to the application layer (use cases) and handles only
 * HTTP concerns such as request/response mapping and status codes.
 */
@RestController
@RequestMapping("/payment-initiation/payment-orders")
public class PaymentOrderController {

    private final InitiatePaymentOrderUseCase initiatePaymentOrderUseCase;
    private final RetrievePaymentOrderUseCase retrievePaymentOrderUseCase;
    private final RetrievePaymentOrderStatusUseCase retrievePaymentOrderStatusUseCase;

    public PaymentOrderController(
            InitiatePaymentOrderUseCase initiatePaymentOrderUseCase,
            RetrievePaymentOrderUseCase retrievePaymentOrderUseCase,
            RetrievePaymentOrderStatusUseCase retrievePaymentOrderStatusUseCase) {
        this.initiatePaymentOrderUseCase = initiatePaymentOrderUseCase;
        this.retrievePaymentOrderUseCase = retrievePaymentOrderUseCase;
        this.retrievePaymentOrderStatusUseCase = retrievePaymentOrderStatusUseCase;
    }

    /**
     * Initiates a new payment order.
     * <p>
     * POST /payment-initiation/payment-orders
     *
     * @param requestDto the payment order initiation request
     * @return the created payment order with HTTP 201
     */
    @PostMapping
    public ResponseEntity<PaymentOrderResourceDto> initiatePaymentOrder(
            @Valid @RequestBody PaymentOrderInitiationRequestDto requestDto) {
        var command = PaymentOrderMapper.toCommand(requestDto);
        PaymentOrder paymentOrder = initiatePaymentOrderUseCase.initiate(command);
        PaymentOrderResourceDto responseDto = PaymentOrderMapper.toResourceDto(paymentOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Retrieves a payment order by its ID.
     * <p>
     * GET /payment-initiation/payment-orders/{paymentOrderId}
     *
     * @param paymentOrderId the payment order identifier
     * @return the payment order with HTTP 200
     */
    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrderResourceDto> retrievePaymentOrder(
            @PathVariable String paymentOrderId) {
        PaymentOrder paymentOrder = retrievePaymentOrderUseCase.retrieveById(paymentOrderId);
        PaymentOrderResourceDto responseDto = PaymentOrderMapper.toResourceDto(paymentOrder);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Retrieves the status of a payment order.
     * <p>
     * GET /payment-initiation/payment-orders/{paymentOrderId}/status
     *
     * @param paymentOrderId the payment order identifier
     * @return the payment order status with HTTP 200
     */
    @GetMapping("/{paymentOrderId}/status")
    public ResponseEntity<PaymentOrderStatusResourceDto> retrievePaymentOrderStatus(
            @PathVariable String paymentOrderId) {
        PaymentOrderStatus status = retrievePaymentOrderStatusUseCase.retrieveStatus(paymentOrderId);

        // For status endpoint, we need to get the last update time from the full payment order
        // This is a design decision: we could either:
        // 1. Have the use case return a status object with timestamp
        // 2. Retrieve the full payment order and extract the timestamp
        // For now, we'll retrieve the full order to get the timestamp
        // TODO: Consider creating a PaymentOrderStatusInfo value object if this becomes a performance issue
        PaymentOrder paymentOrder = retrievePaymentOrderUseCase.retrieveById(paymentOrderId);
        PaymentOrderStatusResourceDto responseDto = PaymentOrderMapper.toStatusResourceDto(
                paymentOrderId,
                status,
                paymentOrder.getLastUpdateDateTime()
        );

        return ResponseEntity.ok(responseDto);
    }
}

