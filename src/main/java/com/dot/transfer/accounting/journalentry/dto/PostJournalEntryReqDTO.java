package com.dot.transfer.accounting.journalentry.dto;

import com.dot.transfer.accounting.journalentry.constant.JournalEntryType;
import com.dot.transfer.accounting.journalentry.domain.JournalEntry;
import com.dot.transfer.accounting.ledger.constant.AccountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record PostJournalEntryReqDTO(
        @NotNull(
                message = "entry type is required"
        )
        JournalEntryType accountType,
        @NotNull(
                message = "transaction id is required"
        )
        UUID transactionId,
        @NotNull(
                message = "description is required"
        )
        String description,
        @NotNull(
                message = "amount is required"
        )
        BigDecimal amount,
        @NotNull(
                message = "transaction date is required"
        )
        Date transactionDate
){

}
