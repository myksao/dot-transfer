package com.dot.transfer.account.saving.dto;

import java.math.BigDecimal;

public record TransactionSummaryResDTO(
        BigDecimal total_deposit,
        BigDecimal total_withdrawal,
        BigDecimal balance,
        String between
) {
}
