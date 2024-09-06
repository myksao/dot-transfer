package com.dot.transfer.account.saving.constant;

import lombok.Getter;

@Getter
public enum InstructionStatusType {
    PENDING(100), COMPLETED(200), FAILED(300);

    private final int value;

    InstructionStatusType(int value) {
        this.value = value;
    }
}
