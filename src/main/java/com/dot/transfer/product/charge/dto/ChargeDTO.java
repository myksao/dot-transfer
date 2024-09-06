package com.dot.transfer.product.charge.dto;

import com.dot.transfer.infrastructure.common.CurrencyType;
import com.dot.transfer.product.charge.constant.ChargeApplyType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record ChargeDTO(
        UUID id,
        String name,
        String description,
        String code,
        ChargeApplyType type,

        BigDecimal commission,

        BigDecimal transaction_fee,

        BigDecimal cap,
        CurrencyType currency,
        Boolean is_deleted,
        Timestamp created_date
        ) {
}
