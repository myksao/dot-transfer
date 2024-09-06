package com.dot.transfer.accounting.ledger.dto;

import com.dot.transfer.accounting.ledger.constant.AccountType;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public record LedgerDTO(
        UUID id,
        String name,
        String account_no,
        AccountType account_type,
        Timestamp created_date,
        String created_by
) {
}
