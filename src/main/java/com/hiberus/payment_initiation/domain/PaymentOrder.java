package com.hiberus.payment_initiation.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Payment order domain entity.
 * <p>
 * Represents a payment order in the domain model. This is the core entity that encapsulates
 * the business concept of a payment order.
 */
public class PaymentOrder {

    private String id;
    private String externalReference;
    private Account debtorAccount;
    private Account creditorAccount;
    private Money instructedAmount;
    private String remittanceInformation;
    private LocalDate requestedExecutionDate;
    private PaymentOrderStatus status;
    private OffsetDateTime creationDateTime;
    private OffsetDateTime lastUpdateDateTime;

    // Private constructor to enforce use of factory methods
    // Public constructor available for infrastructure layer to reconstruct from external systems
    public PaymentOrder() {
    }

    /**
     * Factory method to create a new PaymentOrder.
     */
    public static PaymentOrder create(
            String externalReference,
            Account debtorAccount,
            Account creditorAccount,
            Money instructedAmount,
            String remittanceInformation,
            LocalDate requestedExecutionDate) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.externalReference = externalReference;
        paymentOrder.debtorAccount = debtorAccount;
        paymentOrder.creditorAccount = creditorAccount;
        paymentOrder.instructedAmount = instructedAmount;
        paymentOrder.remittanceInformation = remittanceInformation;
        paymentOrder.requestedExecutionDate = requestedExecutionDate;
        paymentOrder.status = PaymentOrderStatus.INITIATED;
        paymentOrder.creationDateTime = OffsetDateTime.now();
        paymentOrder.lastUpdateDateTime = OffsetDateTime.now();
        return paymentOrder;
    }

    /**
     * Factory method to create a PaymentOrder from an initiation command.
     * <p>
     * This method is used by the application layer to create a domain entity
     * from a command object.
     *
     * @param command the payment order initiation command
     * @return a new PaymentOrder with INITIATED status
     */
    public static PaymentOrder fromInitiationCommand(
            com.hiberus.payment_initiation.application.command.PaymentOrderInitiationCommand command) {
        return create(
                command.getExternalReference(),
                command.getDebtorAccount(),
                command.getCreditorAccount(),
                command.getInstructedAmount(),
                command.getRemittanceInformation(),
                command.getRequestedExecutionDate()
        );
    }

    // Getters
    public String getId() {
        return id;
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

    public PaymentOrderStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public OffsetDateTime getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    // Setters (for infrastructure layer to set id and update status)
    public void setId(String id) {
        this.id = id;
    }

    public void updateStatus(PaymentOrderStatus status) {
        this.status = status;
        this.lastUpdateDateTime = OffsetDateTime.now();
    }

    // Additional setters for infrastructure layer (used when reconstructing from external systems)
    // These are public to allow infrastructure adapters to reconstruct entities from external systems
    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public void setDebtorAccount(Account debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public void setCreditorAccount(Account creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public void setInstructedAmount(Money instructedAmount) {
        this.instructedAmount = instructedAmount;
    }

    public void setRemittanceInformation(String remittanceInformation) {
        this.remittanceInformation = remittanceInformation;
    }

    public void setRequestedExecutionDate(LocalDate requestedExecutionDate) {
        this.requestedExecutionDate = requestedExecutionDate;
    }

    public void setStatus(PaymentOrderStatus status) {
        this.status = status;
    }

    public void setCreationDateTime(OffsetDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setLastUpdateDateTime(OffsetDateTime lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentOrder that = (PaymentOrder) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PaymentOrder{" +
                "id='" + id + '\'' +
                ", externalReference='" + externalReference + '\'' +
                ", status=" + status +
                '}';
    }
}

