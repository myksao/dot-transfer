package com.dot.transfer.accounting.ledger.constant;

import io.hypersistence.utils.hibernate.type.basic.CustomOrdinalEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class AccountTypeConverter extends CustomOrdinalEnumConverter<AccountType> {
    public AccountTypeConverter() {
        super(AccountType.class);
    }

    @Override
    public Integer convertToDatabaseColumn(AccountType accountType) {
        return accountType.getValue();
    }
}
