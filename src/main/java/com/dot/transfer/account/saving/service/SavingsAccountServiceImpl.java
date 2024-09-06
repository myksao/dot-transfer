package com.dot.transfer.account.saving.service;


import com.dot.transfer.account.saving.constant.SavingsAccountTransactionType;
import com.dot.transfer.account.saving.domain.SavingsAccount;
import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.account.saving.dto.*;
import com.dot.transfer.account.saving.repo.SavingsAccountRepo;
import com.dot.transfer.account.saving.repo.SavingsAccountTransactionRepo;
import com.dot.transfer.accounting.journalentry.constant.JournalEntryType;
import com.dot.transfer.accounting.journalentry.dto.PostJournalEntryReqDTO;
import com.dot.transfer.accounting.journalentry.service.JournalEntryService;
import com.dot.transfer.infrastructure.common.GenerateAccountNumber;
import com.dot.transfer.infrastructure.common.PlatformException;
import com.dot.transfer.product.charge.domain.Charge;
import com.dot.transfer.product.charge.repo.ChargeRepo;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private SavingsAccountRepo account;
    private SavingsAccountTransactionRepo transactionRepo;
    private JournalEntryService journalEntryService;
    private ChargeRepo chargeRepo;

    @Autowired
    public SavingsAccountServiceImpl(SavingsAccountRepo account,  SavingsAccountTransactionRepo transactionRepo, JournalEntryService journalEntryService, ChargeRepo chargeRepo) {
        this.account = account;
        this.transactionRepo = transactionRepo;
        this.journalEntryService = journalEntryService;
        this.chargeRepo = chargeRepo;
    }


    @Override
    public CreateSavingsAccountResDTO createSavingsAccount(CreateSavingsAccountReqDTO dto) {
        final Charge charge = this.chargeRepo.findById(UUID.fromString(dto.charge_id())).orElseThrow(() -> new PlatformException("Charge not found", HttpStatus.NOT_FOUND, "Charge not found"));
        final String account_no = this.account.save(new SavingsAccount(GenerateAccountNumber.generateRandomNumberWithSuffix("00"), charge)).getAccountNumber();
        return new CreateSavingsAccountResDTO(account_no);
    }

    @Override
    public SavingsAccountDTO getAccountDetails(String accountNumber) {
        try {
            return this.account.findByAccountNumber(accountNumber).orElseThrow(() -> new PlatformException("Account not found", HttpStatus.NOT_FOUND, "Account not found")).toDTO();

        } catch (PlatformException e) {
            throw new RuntimeException(e);
        }
    }

    @Retryable(retryFor = {OptimisticEntityLockException.class, OptimisticLockException.class, ObjectOptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional
    @Override
    public void deposit(DepositFundReqDTO dto) {
        try {
            final SavingsAccount savingsAccount = this.account.findByAccountNumber(dto.account_no()).orElseThrow(() -> new PlatformException("Account not found", HttpStatus.NOT_FOUND, "Account not found"));

           final SavingsAccountTransaction accountTransaction =  handleDeposit(dto, savingsAccount);

            this.account.save(savingsAccount);

            postJournalEntries(accountTransaction, JournalEntryType.CREDIT);

        } catch (OptimisticLockException | OptimisticEntityLockException| ObjectOptimisticLockingFailureException e) {
            log.error("Error while depositing funds", e);
            throw e;
        } catch (PlatformException e) {
            throw new RuntimeException(e);
        }
    }


    private SavingsAccountTransaction handleDeposit(DepositFundReqDTO dto, SavingsAccount account) {
        final SavingsAccountTransaction transaction = new SavingsAccountTransaction();
        transaction.setSavingsAccount(account);
        transaction.setAmount(dto.amount());
        transaction.setTransactionDate(new Date());
        transaction.setTransactionDescription(dto.note());
        transaction.setTransactionType(SavingsAccountTransactionType.DEPOSIT);
        transaction.setIsReversed(false);

        account.addTransaction(transaction);
        this.transactionRepo.save(transaction);

        final BigDecimal totalDeposit = account.calculateTotalDeposit();
        final BigDecimal totalWithdrawal = account.calculateTotalWithdrawal();

        account.setBalance(totalDeposit.subtract(totalWithdrawal));
        account.setTotalDeposit(totalDeposit);
        account.setTotalWithdrawal(totalWithdrawal);

        return transaction;
    }

    @Retryable(retryFor = {OptimisticEntityLockException.class, OptimisticLockException.class, ObjectOptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional
    @Override
    public void transfer(TransferFundReqDTO dto) throws PlatformException {

        try {
            final SavingsAccount sourceAccount = this.account.findByAccountNumber(dto.from()).orElseThrow(() -> new PlatformException("Account not found", HttpStatus.NOT_FOUND, "Account not found"));
            final SavingsAccount destinationAccount = this.account.findByAccountNumber(dto.to()).orElseThrow(() -> new PlatformException("Account not found", HttpStatus.NOT_FOUND, "Account not found"));

            handleTransfer(dto, sourceAccount, destinationAccount);

            this.account.save(sourceAccount);
            this.account.save(destinationAccount);

        } catch (OptimisticLockException | OptimisticEntityLockException | ObjectOptimisticLockingFailureException e) {
            log.error("Error while transferring funds", e);
            throw e;
        } catch (PlatformException e) {
            throw e;
        }

    }


    private  void handleTransfer(TransferFundReqDTO dto, SavingsAccount sourceAccount, SavingsAccount destinationAccount) throws PlatformException {
        final SavingsAccountTransaction sourceTransaction = getAccountTransaction(sourceAccount, dto, SavingsAccountTransactionType.WITHDRAWAL);

        sourceAccount.addTransaction(sourceTransaction);
        final BigDecimal totalDeposit = sourceAccount.calculateTotalDeposit();
        final BigDecimal totalWithdrawal = sourceAccount.calculateTotalWithdrawal();

        if(totalDeposit.compareTo(totalWithdrawal) < 0) {
            throw new PlatformException("Insufficient funds", HttpStatus.BAD_REQUEST, "Insufficient funds");
        }
        final BigDecimal stotal = totalDeposit.subtract(totalWithdrawal);
        sourceAccount.setBalance(stotal);
        sourceAccount.setTotalDeposit(totalDeposit);
        sourceAccount.setTotalWithdrawal(totalWithdrawal);
        this.transactionRepo.save(sourceTransaction);


        /** Destination **/
        final SavingsAccountTransaction destinationTransaction = getAccountTransaction(destinationAccount, dto, SavingsAccountTransactionType.DEPOSIT);

        destinationAccount.addTransaction(destinationTransaction);
        this.transactionRepo.save(destinationTransaction);


        final BigDecimal totalDepositDestination = destinationAccount.calculateTotalDeposit();
        final BigDecimal totalWithdrawalDestination = destinationAccount.calculateTotalWithdrawal();

        final BigDecimal dtotal = totalDepositDestination.subtract(totalWithdrawalDestination);
        destinationAccount.setBalance(dtotal);
        destinationAccount.setTotalDeposit(totalDepositDestination);
        destinationAccount.setTotalWithdrawal(totalWithdrawalDestination);


        postJournalEntries(sourceTransaction, JournalEntryType.DEBIT);

        postJournalEntries(destinationTransaction, JournalEntryType.CREDIT);

    }

    private static SavingsAccountTransaction getAccountTransaction(SavingsAccount destinationAccount, TransferFundReqDTO dto, SavingsAccountTransactionType deposit) {
        final SavingsAccountTransaction destinationTransaction = new SavingsAccountTransaction();
        destinationTransaction.setSavingsAccount(destinationAccount);
        destinationTransaction.setAmount(dto.amount());
        destinationTransaction.setTransactionDate(new Date());
        destinationTransaction.setTransactionDescription(dto.note());
        destinationTransaction.setTransactionType(deposit);
        destinationTransaction.setIsReversed(false);
        return destinationTransaction;
    }


    public void postJournalEntries(SavingsAccountTransaction transaction, JournalEntryType Journal) throws PlatformException {
        PostJournalEntryReqDTO dto = new PostJournalEntryReqDTO(
            Journal,
                transaction.getId(),
            transaction.getTransactionDescription(), transaction.getAmount(),
            transaction.getTransactionDate()
        );
        this.journalEntryService.postJournalEntry(dto);
    }

}
