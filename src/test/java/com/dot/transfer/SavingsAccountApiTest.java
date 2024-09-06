package com.dot.transfer;

import com.dot.transfer.account.saving.api.SavingsAccountApi;
import com.dot.transfer.account.saving.constant.SavingsAccountStatusType;
import com.dot.transfer.account.saving.service.SavingsAccountService;
import com.dot.transfer.account.saving.dto.*;
import com.dot.transfer.infrastructure.common.ApiResponse;
import com.dot.transfer.infrastructure.common.PlatformException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SavingsAccountApiTest {

    @Mock
    private SavingsAccountService savingsAccountService;

    @InjectMocks
    private SavingsAccountApi savingsAccountApi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSavingsAccount_Success() {
        CreateSavingsAccountReqDTO reqDTO = new CreateSavingsAccountReqDTO(
                "25b5a99c-3d4b-40ef-a9f2-04b3490ddc14"
        );
        CreateSavingsAccountResDTO resDTO = new CreateSavingsAccountResDTO("12345");
        when(savingsAccountService.createSavingsAccount(reqDTO)).thenReturn(resDTO);

        ResponseEntity<ApiResponse<CreateSavingsAccountResDTO>> response = savingsAccountApi.createSavingsAccount(reqDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(resDTO, response.getBody().getData());
        verify(savingsAccountService, times(1)).createSavingsAccount(reqDTO);
    }

    @Test
    void getAccountDetails_Success() {
        String accountNo = "12345";
        SavingsAccountDTO accountDTO = new SavingsAccountDTO(UUID.fromString("25b5a99c-3d4b-40ef-a9f2-04b3490ddc14"), accountNo, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, SavingsAccountStatusType.ACTIVE, new Date());
        when(savingsAccountService.getAccountDetails(accountNo)).thenReturn(accountDTO);

        ResponseEntity<ApiResponse<SavingsAccountDTO>> response = savingsAccountApi.getAccountDetails(accountNo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(accountDTO, response.getBody().getData());
        verify(savingsAccountService, times(1)).getAccountDetails(accountNo);
    }

    @Test
    void deposit_Success() {
        DepositFundReqDTO reqDTO = new DepositFundReqDTO("12345", BigDecimal.valueOf(50.0), "Deposit");
        doNothing().when(savingsAccountService).deposit(reqDTO);

        ResponseEntity<ApiResponse<String>> response = savingsAccountApi.deposit(reqDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deposit Successful", response.getBody().getData());
        verify(savingsAccountService, times(1)).deposit(reqDTO);
    }

    @Test
    void transfer_Success() throws PlatformException {
        TransferFundReqDTO reqDTO = new TransferFundReqDTO( "12345", "123456", BigDecimal.valueOf(50.0), "Transfer");
        doNothing().when(savingsAccountService).transfer(reqDTO);

        ResponseEntity<ApiResponse<String>> response = savingsAccountApi.transfer(reqDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transfer Successful", response.getBody().getData());
        verify(savingsAccountService, times(1)).transfer(reqDTO);
    }

    @Test
    void transfer_ThrowsPlatformException() throws PlatformException {
        TransferFundReqDTO reqDTO = new TransferFundReqDTO("12345", "123456", BigDecimal.valueOf(50.0), "Transfer");
        doThrow(new PlatformException("Transfer failed", HttpStatus.BAD_REQUEST, "TF")).when(savingsAccountService).transfer(reqDTO);

        PlatformException exception = assertThrows(PlatformException.class, () -> {
            savingsAccountApi.transfer(reqDTO);
        });

        assertEquals("Transfer failed", exception.getMessage());
        verify(savingsAccountService, times(1)).transfer(reqDTO);
    }
}