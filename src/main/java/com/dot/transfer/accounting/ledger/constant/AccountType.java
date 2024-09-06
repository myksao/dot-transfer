package com.dot.transfer.accounting.ledger.constant;

import com.dot.transfer.infrastructure.common.PlatformException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AccountType {
    ASSET(100), LIABILITY(200), EQUITY(300), REVENUE(400), EXPENSE(500);

    private final int value;

    AccountType(int value) {
        this.value = value;
    }

    public static AccountType fromValue(int value) {
        for (AccountType type : AccountType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new PlatformException("Invalid value: " + value, HttpStatus.BAD_REQUEST, "Invalid value: " + value);
    }
}
