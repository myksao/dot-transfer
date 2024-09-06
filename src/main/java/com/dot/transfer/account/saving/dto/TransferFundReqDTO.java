package com.dot.transfer.account.saving.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferFundReqDTO(
        @NotNull(
                message = "From account number is required"
        )
        @Size(min = 9, max = 10)
        String from,
        @NotNull(
                message = "To account number is required"
        ) @Size(min = 9, max = 10) String to,
        @NotNull(
                message = "Amount is required"
        ) @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        ) BigDecimal amount,
        @NotNull(
                message = "Note is required"
        ) @Size(min = 1, max = 255
        ) String note
) {
}
