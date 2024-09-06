package com.dot.transfer.product.charge.dto;

import com.dot.transfer.infrastructure.common.CurrencyType;
import com.dot.transfer.product.charge.constant.ChargeApplyType;
import com.dot.transfer.product.charge.domain.Charge;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateChargeReqDTO(
        @NotNull(
                message = "Name is required"
        ) @Size(min = 1, max = 100) String name,
        @NotNull(
                message = "Description is required"
        ) @Size(min = 1, max = 100) String description,
        @NotNull(
                message = "Code is required"
        ) @Size(min = 1, max = 100)
        String code,
        @NotNull(
                message = "Type is required"
        ) int type,
        @NotNull(
                message = "Currency is required"
        ) int currency,
        @NotNull(
                message = "Commission is required"
        ) @DecimalMin(value = "0.0") BigDecimal commission,
        @NotNull(
                message = "Transaction Fee is required"
        ) @DecimalMin(value = "0.0") BigDecimal transactionFee,
        @NotNull(
                message = "Cap is required"
        ) @DecimalMin(value = "0.0") BigDecimal cap
) {

    public Charge toDomain() {
        return new Charge(
                name,
                description,
                code,
                ChargeApplyType.fromValue(type),
                commission,
                transactionFee,
                cap,
                CurrencyType.fromValue(currency)
        );
    }
}
