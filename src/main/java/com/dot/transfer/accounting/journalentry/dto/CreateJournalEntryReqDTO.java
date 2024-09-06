package com.dot.transfer.accounting.journalentry.dto;

import com.dot.transfer.accounting.journalentry.constant.JournalEntryType;
import com.dot.transfer.accounting.journalentry.domain.JournalEntry;
import com.dot.transfer.accounting.ledger.domain.AccountLedger;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateJournalEntryReqDTO(
       @NotNull(
                message = "Ledger id is required"
       )
       @Pattern(regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Invalid UUID format")
       String ledger_id,
       @NotNull(
                message = "Transaction id is required"
       )
       @Pattern(regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Invalid UUID format")
       String transaction_id,
       @NotNull(
                message = "Type is required"
       ) int type,
        @NotNull(
                message = "Amount is required"
        ) BigDecimal amount,
       @NotNull(
                message = "Description is required"
       ) String description
) {


       public UUID getLedgerId() {
              return UUID.fromString(this.ledger_id);
       }

         public UUID getTransactionId() {
                  return UUID.fromString(this.transaction_id);
         }
}
