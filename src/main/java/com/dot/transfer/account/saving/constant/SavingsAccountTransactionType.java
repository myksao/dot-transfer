package com.dot.transfer.account.saving.constant;

import lombok.Getter;

@Getter
public enum SavingsAccountTransactionType {

    DEPOSIT(100),
    WITHDRAWAL(200);

    private final int value;


    SavingsAccountTransactionType(int value) {
        this.value = value;
    }

}
