package com.dot.transfer.infrastructure.common;

import io.hypersistence.utils.hibernate.type.basic.CustomOrdinalEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class CurrencyConverter extends CustomOrdinalEnumConverter<CurrencyType> {
    public CurrencyConverter() {
        super(CurrencyType.class);
    }

    @Override
    public Integer convertToDatabaseColumn(CurrencyType currencyType) {
        return currencyType.getValue();
    }
}
