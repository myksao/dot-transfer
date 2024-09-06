package com.dot.transfer.account.saving.api;

import com.dot.transfer.account.saving.domain.SavingsAccount;
import com.dot.transfer.account.saving.dto.*;
import com.dot.transfer.account.saving.service.SavingsAccountService;
import com.dot.transfer.infrastructure.common.ApiResponse;
import com.dot.transfer.infrastructure.common.PlatformException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/savings")
@Slf4j
public class SavingsAccountApi {

    final private SavingsAccountService savingsAccountService;


    @Autowired
    public SavingsAccountApi(final SavingsAccountService savingsAccountService) {
        this.savingsAccountService = savingsAccountService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CreateSavingsAccountResDTO>> createSavingsAccount(@Valid @RequestBody CreateSavingsAccountReqDTO createSavingsAccountReqDTO) {
        log.info("Savings Account Created");
        final ApiResponse<CreateSavingsAccountResDTO> res = ApiResponse.success(this.savingsAccountService.createSavingsAccount(createSavingsAccountReqDTO));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{account_no}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SavingsAccountDTO>> getAccountDetails(@PathVariable String account_no) {
        log.info("Savings Account Details");
        return new ResponseEntity<>(ApiResponse.success(this.savingsAccountService.getAccountDetails(account_no)), HttpStatus.CREATED);
    }

    @PostMapping(path = "/deposit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> deposit(@Valid @RequestBody DepositFundReqDTO depositFundReqDTO) {
        log.info("Deposit Fund");
        this.savingsAccountService.deposit(depositFundReqDTO);
        return ResponseEntity.ok(ApiResponse.success("Deposit Successful"));
    }

    @PostMapping(path = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> transfer(@Valid @RequestBody TransferFundReqDTO transferFundReqDTO) throws PlatformException {
        log.info("Transfer Fund");
        this.savingsAccountService.transfer(transferFundReqDTO);
        return ResponseEntity.ok(ApiResponse.success("Transfer Successful"));
    }

}
