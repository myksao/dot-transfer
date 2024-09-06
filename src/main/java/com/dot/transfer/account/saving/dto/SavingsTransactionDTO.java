package com.dot.transfer.account.saving.dto;

import com.dot.transfer.account.saving.constant.SavingsAccountTransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record SavingsTransactionDTO(
        UUID id,
        boolean is_reversed,
        SavingsAccountTransactionType transaction_type,
        Date transaction_date,
        BigDecimal amount,
        BigDecimal commission,
        BigDecimal transaction_fee,
        String note,
        String transaction_description,
        String transaction_status,
        Date created_date,
        Date last_modified_date
) {
}
