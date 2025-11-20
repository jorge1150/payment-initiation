package com.hiberus.payment_initiation.api.error;

import com.hiberus.payment_initiation.api.dto.ErrorResponseDto;
import com.hiberus.payment_initiation.shared.exception.PaymentOrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for REST API.
 * <p>
 * This handler provides centralized exception handling for all REST controllers,
 * ensuring consistent error responses across the API. It follows banking industry
 * best practices by not exposing internal implementation details to clients.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles PaymentOrderNotFoundException.
     * <p>
     * Maps to HTTP 404 NOT_FOUND with error code PAYMENT_ORDER_NOT_FOUND.
     *
     * @param ex the exception
     * @return error response with 404 status
     */
    @ExceptionHandler(PaymentOrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePaymentOrderNotFoundException(PaymentOrderNotFoundException ex) {
        log.warn("Payment order not found: {}", ex.getMessage());

        String message = ex.getMessage();
        List<String> details = new ArrayList<>();
        
        // Extract paymentOrderId from message if possible
        // Message format: "Payment order not found with id: {paymentOrderId}"
        if (message != null && message.contains("id: ")) {
            String paymentOrderId = message.substring(message.lastIndexOf("id: ") + 4);
            details.add("Payment order ID: " + paymentOrderId);
        } else {
            details.add(message != null ? message : "Payment order not found");
        }

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "PAYMENT_ORDER_NOT_FOUND",
                message != null ? message : "Payment order not found",
                details
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles validation errors from @Valid annotations.
     * <p>
     * Maps to HTTP 400 BAD_REQUEST with error code VALIDATION_ERROR.
     *
     * @param ex the validation exception
     * @return error response with 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String detail = String.format("%s: %s", error.getField(), error.getDefaultMessage());
            details.add(detail);
        });

        String message = "Request validation failed";
        if (!details.isEmpty()) {
            message = "Request validation failed: " + details.size() + " error(s)";
        }

        log.warn("Validation error: {} field(s) failed validation", details.size());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "VALIDATION_ERROR",
                message,
                details
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles all other exceptions (catch-all).
     * <p>
     * Maps to HTTP 500 INTERNAL_SERVER_ERROR with error code INTERNAL_ERROR.
     * This handler ensures that no internal implementation details are exposed
     * to the client, following security best practices.
     *
     * @param ex the exception
     * @return error response with 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        String exceptionType = ex.getClass().getSimpleName();
        log.error("Unexpected error occurred: {}", exceptionType, ex);

        List<String> details = new ArrayList<>();
        details.add("Error type: " + exceptionType);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "INTERNAL_ERROR",
                "Unexpected error while processing the request",
                details
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

