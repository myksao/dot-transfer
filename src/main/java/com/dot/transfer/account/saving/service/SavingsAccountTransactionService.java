package com.dot.transfer.account.saving.service;

import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.account.saving.dto.FetchSavingsAccountTransactionsReqDTO;
import com.dot.transfer.account.saving.dto.SavingsTransactionDTO;
import com.dot.transfer.account.saving.dto.TransactionSummaryReqDTO;
import com.dot.transfer.account.saving.dto.TransactionSummaryResDTO;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface SavingsAccountTransactionService {

    Page<SavingsTransactionDTO> getTransactions(FetchSavingsAccountTransactionsReqDTO fetchSavingsAccountTransactionsReqDTO);

    TransactionSummaryResDTO getTransactionSummary(TransactionSummaryReqDTO dto);
}
