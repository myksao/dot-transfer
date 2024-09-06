package com.dot.transfer.accounting.ledger.dto;

import com.dot.transfer.accounting.ledger.constant.AccountType;
import com.dot.transfer.accounting.ledger.domain.AccountLedger;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountLedgerReqDTO(
        @NotNull(
                message = "Name is required"
        ) @Size(min = 1, max = 255) String name,
        @NotNull(
                message = "Account number is required"
        ) @Size(min = 1, max = 255) String account_no,
        @NotNull(
                message = "Account type is required"
        ) int account_type
) {

    public AccountLedger toDomain() {
        return new AccountLedger(name, account_no, AccountType.fromValue(account_type));
    }
}
