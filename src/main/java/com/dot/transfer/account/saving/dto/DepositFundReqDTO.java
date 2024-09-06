package com.dot.transfer.account.saving.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DepositFundReqDTO(
        @NotNull(
                message = "Account number is required"
        )
        @Size(
                min = 9,
                max = 10,
                message = "Account number must be 10 characters"
        )
        String account_no,
       @NotNull(
                message = "Amount is required"
       ) BigDecimal amount,
       @NotNull(
                message = "Note is required"
       ) String note
) {

}
