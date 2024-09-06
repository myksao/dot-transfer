package com.dot.transfer.product.charge.constant;

import com.dot.transfer.infrastructure.common.PlatformException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChargeApplyType {
    FLAT(100), PERCENTAGE(200);

    private final int value;

    ChargeApplyType(int value) {
        this.value = value;
    }

    // Method to get enum from value
    public static ChargeApplyType fromValue(int value) {
        for (ChargeApplyType type : ChargeApplyType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new PlatformException("Invalid value: " + value, HttpStatus.BAD_REQUEST, "Invalid value: " + value);
    }
}
