package com.dot.transfer.account.saving.service;


import com.dot.transfer.account.saving.domain.SavingsAccount;
import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.account.saving.dto.FetchSavingsAccountTransactionsReqDTO;
import com.dot.transfer.account.saving.dto.SavingsTransactionDTO;
import com.dot.transfer.account.saving.dto.TransactionSummaryReqDTO;
import com.dot.transfer.account.saving.dto.TransactionSummaryResDTO;
import com.dot.transfer.account.saving.repo.SavingsAccountRepo;
import com.dot.transfer.account.saving.repo.SavingsAccountTransactionRepo;
import com.dot.transfer.infrastructure.common.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavingsAccountTransactionServiceImpl implements  SavingsAccountTransactionService {

    public SavingsAccountTransactionRepo savingsAccountTransactionRepo;
    public SavingsAccountRepo savingsAccountRepo;

    @Autowired
    public SavingsAccountTransactionServiceImpl(SavingsAccountTransactionRepo savingsAccountTransactionRepo,SavingsAccountRepo savingsAccountRepo) {
        this.savingsAccountTransactionRepo = savingsAccountTransactionRepo;
        this.savingsAccountRepo  =  savingsAccountRepo;
    }


    @Override
    public Page<SavingsTransactionDTO> getTransactions(FetchSavingsAccountTransactionsReqDTO fetchSavingsAccountTransactionsReqDTO) {

        SavingsAccount savingsAccount = this.savingsAccountRepo.findByAccountNumber(fetchSavingsAccountTransactionsReqDTO.account_no()).orElseThrow(
                () -> new PlatformException("Savings Account not found", HttpStatus.NOT_FOUND, "")
        );
        Pageable pageable = PageRequest.of(fetchSavingsAccountTransactionsReqDTO.page(), fetchSavingsAccountTransactionsReqDTO.size());

        return this.savingsAccountTransactionRepo.findBySavingsAccountId(savingsAccount.getId(),pageable).map(
                savingsAccountTransaction -> new SavingsTransactionDTO(
                        savingsAccountTransaction.getId(),
                        savingsAccountTransaction.getIsReversed(),
                        savingsAccountTransaction.getTransactionType(),
                        savingsAccountTransaction.getTransactionDate(),
                        savingsAccountTransaction.getAmount(),
                        savingsAccountTransaction.getCommission(),
                        savingsAccountTransaction.getTransactionFee(),
                        savingsAccountTransaction.getNote(),
                        savingsAccountTransaction.getTransactionDescription(),
                        savingsAccountTransaction.getTransactionStatus(),
                        savingsAccountTransaction.getCreatedDate(),
                        savingsAccountTransaction.getUpdatedDate()
        ));
    }

    @Override
    public TransactionSummaryResDTO getTransactionSummary(TransactionSummaryReqDTO dto) {
            LocalDate startDate = LocalDate.parse(dto.start_date());
            LocalDate endDate = LocalDate.parse(dto.end_date());


        final Optional<BigDecimal> deposit = this.savingsAccountTransactionRepo.fetchTotalDepositForSpecificDate(Date.valueOf(startDate),Date.valueOf(endDate));
        final Optional<BigDecimal> withdrawal = this.savingsAccountTransactionRepo.fetchTotalWithdrawalForSpecificDate(Date.valueOf(startDate),Date.valueOf(endDate));


            return new TransactionSummaryResDTO(
                    deposit.orElse(BigDecimal.ZERO),
                    withdrawal.orElse(BigDecimal.ZERO),
                    deposit.orElse(BigDecimal.ZERO).subtract(withdrawal.orElse(BigDecimal.ZERO)),
                    startDate + " to " + endDate
            );
    }
}
