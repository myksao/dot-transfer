package com.dot.transfer.product.charge.constant;

import io.hypersistence.utils.hibernate.type.basic.CustomOrdinalEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class ChargeApplyTypeConverter extends CustomOrdinalEnumConverter<ChargeApplyType> {
    public ChargeApplyTypeConverter() {
        super(ChargeApplyType.class);
    }

    @Override
    public Integer convertToDatabaseColumn(ChargeApplyType chargeApplyType) {
        return chargeApplyType.getValue();
    }
}
