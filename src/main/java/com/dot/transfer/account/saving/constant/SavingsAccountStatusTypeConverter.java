package com.dot.transfer.account.saving.constant;

import io.hypersistence.utils.hibernate.type.basic.CustomOrdinalEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class SavingsAccountStatusTypeConverter  extends CustomOrdinalEnumConverter<SavingsAccountStatusType> {
    public SavingsAccountStatusTypeConverter() {
        super(SavingsAccountStatusType.class);
    }

    @Override
    public Integer convertToDatabaseColumn(SavingsAccountStatusType savingsAccountStatusType) {
        return savingsAccountStatusType.getValue();
    }
}