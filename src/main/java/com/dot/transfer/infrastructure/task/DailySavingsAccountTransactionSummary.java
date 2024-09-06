package com.dot.transfer.infrastructure.task;


import com.dot.transfer.account.saving.dto.TransactionSummaryReqDTO;
import com.dot.transfer.account.saving.dto.TransactionSummaryResDTO;
import com.dot.transfer.account.saving.service.SavingsAccountTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class DailySavingsAccountTransactionSummary {

    private  final SavingsAccountTransactionService transactionService;

    @Autowired
    public DailySavingsAccountTransactionSummary( SavingsAccountTransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void task1() {
        // Task logic goes here
        log.info("Task 2 executed.");

        LocalDate yesterday = LocalDate.now().minusDays(1);
        TransactionSummaryResDTO summary = this.transactionService.getTransactionSummary(new TransactionSummaryReqDTO(yesterday.toString(), yesterday.toString()));

        log.info("Transaction Summary for [{}]: {}",yesterday, summary);
        //Transaction Summary for [2024-09-05]: TransactionSummaryResDTO[total_deposit=0, total_withdrawal=0, balance=0, between=2024-09-05 to 2024-09-05]
    }
}
