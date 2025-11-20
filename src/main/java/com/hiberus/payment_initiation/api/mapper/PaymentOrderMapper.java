package com.hiberus.payment_initiation.api.mapper;

import com.hiberus.payment_initiation.api.dto.AccountReferenceDto;
import com.hiberus.payment_initiation.api.dto.AmountDto;
import com.hiberus.payment_initiation.api.dto.PaymentOrderInitiationRequestDto;
import com.hiberus.payment_initiation.api.dto.PaymentOrderResourceDto;
import com.hiberus.payment_initiation.api.dto.PaymentOrderStatusResourceDto;
import com.hiberus.payment_initiation.application.command.PaymentOrderInitiationCommand;
import com.hiberus.payment_initiation.domain.Account;
import com.hiberus.payment_initiation.domain.Money;
import com.hiberus.payment_initiation.domain.PaymentOrder;
import com.hiberus.payment_initiation.domain.PaymentOrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Mapper for converting between API DTOs and domain objects.
 * <p>
 * This mapper handles the transformation between the API layer (DTOs) and the domain/application layers.
 * It ensures that the API layer remains decoupled from the domain model.
 */
public class PaymentOrderMapper {

    /**
     * Converts a PaymentOrderInitiationRequestDto to a PaymentOrderInitiationCommand.
     *
     * @param dto the request DTO
     * @return the command object
     */
    public static PaymentOrderInitiationCommand toCommand(PaymentOrderInitiationRequestDto dto) {
        Account debtorAccount = new Account(dto.getDebtorAccount().getIban());
        Account creditorAccount = new Account(dto.getCreditorAccount().getIban());
        Money instructedAmount = new Money(
                BigDecimal.valueOf(dto.getInstructedAmount().getAmount()),
                dto.getInstructedAmount().getCurrency()
        );

        return new PaymentOrderInitiationCommand(
                dto.getExternalReference(),
                debtorAccount,
                creditorAccount,
                instructedAmount,
                dto.getRemittanceInformation(),
                dto.getRequestedExecutionDate()
        );
    }

    /**
     * Converts a PaymentOrder domain entity to a PaymentOrderResourceDto.
     *
     * @param paymentOrder the domain entity
     * @return the resource DTO
     */
    public static PaymentOrderResourceDto toResourceDto(PaymentOrder paymentOrder) {
        PaymentOrderResourceDto dto = new PaymentOrderResourceDto();
        dto.setPaymentOrderId(paymentOrder.getId());
        dto.setStatus(paymentOrder.getStatus().name());
        dto.setExternalReference(paymentOrder.getExternalReference());
        dto.setDebtorAccount(toAccountReferenceDto(paymentOrder.getDebtorAccount()));
        dto.setCreditorAccount(toAccountReferenceDto(paymentOrder.getCreditorAccount()));
        dto.setInstructedAmount(toAmountDto(paymentOrder.getInstructedAmount()));
        dto.setRemittanceInformation(paymentOrder.getRemittanceInformation());
        dto.setRequestedExecutionDate(paymentOrder.getRequestedExecutionDate());
        dto.setCreationDateTime(paymentOrder.getCreationDateTime());
        dto.setLastUpdateDateTime(paymentOrder.getLastUpdateDateTime());
        return dto;
    }

    /**
     * Converts payment order status information to a PaymentOrderStatusResourceDto.
     *
     * @param paymentOrderId the payment order ID
     * @param status the payment order status
     * @param lastUpdateDateTime the last update timestamp
     * @return the status resource DTO
     */
    public static PaymentOrderStatusResourceDto toStatusResourceDto(
            String paymentOrderId,
            PaymentOrderStatus status,
            OffsetDateTime lastUpdateDateTime) {
        PaymentOrderStatusResourceDto dto = new PaymentOrderStatusResourceDto();
        dto.setPaymentOrderId(paymentOrderId);
        dto.setStatus(status.name());
        dto.setLastUpdateDateTime(lastUpdateDateTime);
        return dto;
    }

    private static AccountReferenceDto toAccountReferenceDto(Account account) {
        return new AccountReferenceDto(account.getIban());
    }

    private static AmountDto toAmountDto(Money money) {
        return new AmountDto(money.getAmount().doubleValue(), money.getCurrency());
    }
}

