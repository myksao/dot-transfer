package com.dot.transfer.account.saving.dto;

import com.dot.transfer.account.saving.constant.SavingsAccountStatusType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record SavingsAccountDTO(
        UUID id,
        String account_no,
        BigDecimal balance,
        BigDecimal total_deposit,
        BigDecimal total_withdrawal,
        SavingsAccountStatusType status,
        Date created_date
        ) {
}
