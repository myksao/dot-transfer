package com.dot.transfer.account.saving.constant;

import lombok.Getter;

@Getter
public enum SavingsAccountStatusType {
    ACTIVE(100), INACTIVE(200), CLOSED(300);

    private final int value;

    SavingsAccountStatusType(int value){
        this.value = value;
    }

}
