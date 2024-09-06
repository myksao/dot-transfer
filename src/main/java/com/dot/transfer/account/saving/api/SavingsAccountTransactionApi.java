package com.dot.transfer.account.saving.api;


import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.account.saving.dto.FetchSavingsAccountTransactionsReqDTO;
import com.dot.transfer.account.saving.dto.SavingsTransactionDTO;
import com.dot.transfer.account.saving.dto.TransactionSummaryReqDTO;
import com.dot.transfer.account.saving.dto.TransactionSummaryResDTO;
import com.dot.transfer.account.saving.service.SavingsAccountTransactionService;
import com.dot.transfer.infrastructure.common.ApiResponse;
import com.dot.transfer.infrastructure.common.PlatformException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/api/v1/savings/transactions")
@Slf4j
@RequiredArgsConstructor
public class SavingsAccountTransactionApi {

    private SavingsAccountTransactionService savingsAccountTransactionService;

    @Autowired
    public SavingsAccountTransactionApi(SavingsAccountTransactionService savingsAccountTransactionService) {
        this.savingsAccountTransactionService = savingsAccountTransactionService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<SavingsTransactionDTO>>> getTransactions(@Valid  FetchSavingsAccountTransactionsReqDTO fetchSavingsAccountTransactionsReqDTO) {
        log.info("Savings Account Transactions");
        return ResponseEntity.ok(ApiResponse.success(this.savingsAccountTransactionService.getTransactions(fetchSavingsAccountTransactionsReqDTO)));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<TransactionSummaryResDTO>> getTransactionSummary(@Valid  TransactionSummaryReqDTO dto) throws PlatformException {

        return ResponseEntity.ok(ApiResponse.success(this.savingsAccountTransactionService.getTransactionSummary(dto)));
    }

}
