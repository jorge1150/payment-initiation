package com.hiberus.payment_initiation.application.command;

import com.hiberus.payment_initiation.domain.Account;
import com.hiberus.payment_initiation.domain.Money;

import java.time.LocalDate;

/**
 * Command for initiating a payment order.
 * <p>
 * This command object encapsulates the data needed to initiate a new payment order.
 * It uses domain value objects (Account, Money) instead of DTOs to maintain
 * independence from the API layer.
 */
public class PaymentOrderInitiationCommand {

    private final String externalReference;
    private final Account debtorAccount;
    private final Account creditorAccount;
    private final Money instructedAmount;
    private final String remittanceInformation;
    private final LocalDate requestedExecutionDate;

    public PaymentOrderInitiationCommand(
            String externalReference,
            Account debtorAccount,
            Account creditorAccount,
            Money instructedAmount,
            String remittanceInformation,
            LocalDate requestedExecutionDate) {
        this.externalReference = externalReference;
        this.debtorAccount = debtorAccount;
        this.creditorAccount = creditorAccount;
        this.instructedAmount = instructedAmount;
        this.remittanceInformation = remittanceInformation;
        this.requestedExecutionDate = requestedExecutionDate;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public Account getDebtorAccount() {
        return debtorAccount;
    }

    public Account getCreditorAccount() {
        return creditorAccount;
    }

    public Money getInstructedAmount() {
        return instructedAmount;
    }

    public String getRemittanceInformation() {
        return remittanceInformation;
    }

    public LocalDate getRequestedExecutionDate() {
        return requestedExecutionDate;
    }
}

