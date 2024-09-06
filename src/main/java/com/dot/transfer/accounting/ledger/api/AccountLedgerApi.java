package com.dot.transfer.accounting.ledger.api;


import com.dot.transfer.accounting.ledger.domain.AccountLedger;
import com.dot.transfer.accounting.ledger.dto.CreateAccountLedgerReqDTO;
import com.dot.transfer.accounting.ledger.dto.CreateAccountLedgerResDTO;
import com.dot.transfer.accounting.ledger.dto.FetchAcctLedgerReqDTO;
import com.dot.transfer.accounting.ledger.dto.LedgerDTO;
import com.dot.transfer.accounting.ledger.service.AccountLedgerService;
import com.dot.transfer.infrastructure.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping(path = "/api/v1/ledger")
@Slf4j
@RequiredArgsConstructor
public class AccountLedgerApi {

    private  AccountLedgerService accountLedgerService;


    @Autowired
    public AccountLedgerApi(AccountLedgerService accountLedgerService) {
        this.accountLedgerService = accountLedgerService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CreateAccountLedgerResDTO>> createAccountLedger(@Valid @RequestBody CreateAccountLedgerReqDTO reqDTO) {
        log.info("Account Ledger Created");
        return ResponseEntity.ok(ApiResponse.success(this.accountLedgerService.createAccountLedger(reqDTO)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<LedgerDTO>>> getAccountLedgers(@Valid  FetchAcctLedgerReqDTO reqDTO) {
        log.info("Account Ledger Details");
        return ResponseEntity.ok(ApiResponse.success(this.accountLedgerService.getAccountLedgers(reqDTO)));
    }
}
