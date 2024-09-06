package com.dot.transfer.accounting.journalentry.constant;

import com.dot.transfer.infrastructure.common.PlatformException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JournalEntryType {
    DEBIT(100), CREDIT(200);

    private final int value;

    public int getValue() {
        return this.value;
    }

    JournalEntryType(int value) {
        this.value = value;
    }

    public static JournalEntryType fromValue(int value) {
        for (JournalEntryType type : JournalEntryType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new PlatformException("Invalid value: " + value, HttpStatus.BAD_REQUEST, "Invalid value: " + value);
    }
}
