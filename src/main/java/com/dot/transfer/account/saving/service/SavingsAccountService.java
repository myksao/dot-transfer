package com.dot.transfer.account.saving.service;


import com.dot.transfer.account.saving.domain.SavingsAccount;
import com.dot.transfer.account.saving.dto.*;
import com.dot.transfer.infrastructure.common.PlatformException;

public interface SavingsAccountService {

    CreateSavingsAccountResDTO createSavingsAccount(CreateSavingsAccountReqDTO dto);

    SavingsAccountDTO getAccountDetails(String accountNumber);

    void deposit(DepositFundReqDTO dto);

    void transfer(TransferFundReqDTO dto) throws PlatformException;
}
