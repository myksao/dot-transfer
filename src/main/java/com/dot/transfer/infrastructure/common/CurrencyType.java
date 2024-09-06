package com.dot.transfer.infrastructure.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CurrencyType {
    NGN(100), USD(200), GBP(300), EUR(400);

    private final int value;

    CurrencyType(int value) {
        this.value = value;
    }

    public static CurrencyType fromValue(int value) {
        for (CurrencyType type : CurrencyType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new PlatformException("Invalid value: " + value, HttpStatus.BAD_REQUEST, "Invalid value: " + value);
    }
}
