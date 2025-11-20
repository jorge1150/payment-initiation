package com.hiberus.payment_initiation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Error response DTO.
 * <p>
 * Standard error response structure returned by the API when an error occurs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private String code;

    private String message;

    private List<String> details;
}

