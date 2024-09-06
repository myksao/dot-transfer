package com.dot.transfer.account.saving.constant;

import io.hypersistence.utils.hibernate.type.basic.CustomOrdinalEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class SavingsAccountTransactionTypeConverter extends CustomOrdinalEnumConverter<SavingsAccountTransactionType> {

    public SavingsAccountTransactionTypeConverter() {
        super(SavingsAccountTransactionType.class);
    }

    @Override
    public Integer convertToDatabaseColumn(SavingsAccountTransactionType transactionType) {
        return transactionType.getValue();
    }
}