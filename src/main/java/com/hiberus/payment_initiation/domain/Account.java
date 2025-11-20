package com.hiberus.payment_initiation.domain;

import java.util.Objects;

/**
 * Account value object.
 * <p>
 * Represents a bank account identified by its IBAN (International Bank Account Number).
 * This is a value object, so it should be immutable.
 */
public class Account {

    private final String iban;

    public Account(String iban) {
        if (iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("IBAN cannot be null or blank");
        }
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(iban, account.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public String toString() {
        return "Account{iban='" + iban + "'}";
    }
}

