package com.dot.transfer.accounting.journalentry.service;

import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.account.saving.repo.SavingsAccountTransactionRepo;
import com.dot.transfer.accounting.journalentry.constant.JournalEntryType;
import com.dot.transfer.accounting.journalentry.domain.JournalEntry;
import com.dot.transfer.accounting.journalentry.dto.CreateJournalEntryReqDTO;
import com.dot.transfer.accounting.journalentry.dto.CreateJournalEntryResDTO;
import com.dot.transfer.accounting.journalentry.dto.PostJournalEntryReqDTO;
import com.dot.transfer.accounting.journalentry.repo.JournalEntryRepo;
import com.dot.transfer.accounting.ledger.constant.AccountType;
import com.dot.transfer.accounting.ledger.domain.AccountLedger;
import com.dot.transfer.accounting.ledger.repo.AccountLedgerRepo;
import com.dot.transfer.infrastructure.common.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JournalEntryServiceImpl implements  JournalEntryService{

    private JournalEntryRepo journalEntryRepo;
    private AccountLedgerRepo accountLedgerRepo;
    private SavingsAccountTransactionRepo savingsAccountTransactionRepo;

    @Autowired
    public JournalEntryServiceImpl(JournalEntryRepo journalEntryRepo, AccountLedgerRepo accountLedgerRepo, SavingsAccountTransactionRepo savingsAccountTransactionRepo) {
        this.journalEntryRepo = journalEntryRepo;
        this.accountLedgerRepo = accountLedgerRepo;
        this.savingsAccountTransactionRepo = savingsAccountTransactionRepo;
    }

    @Transactional
    @Override
    public CreateJournalEntryResDTO createJournalEntry(CreateJournalEntryReqDTO createJournalEntryReqDto)  {
        final AccountLedger accountLedger = this.accountLedgerRepo.findById(createJournalEntryReqDto.getLedgerId())
                .orElseThrow(() -> new PlatformException("Account Ledger not found", HttpStatus.NOT_FOUND, "Account Ledger not found"));

        final SavingsAccountTransaction savingsAccountTransaction = this.savingsAccountTransactionRepo.findById(createJournalEntryReqDto.getTransactionId())
                .orElseThrow(() -> new PlatformException("Savings Account Transaction not found", HttpStatus.NOT_FOUND, "Savings Account Transaction not found"));
        final UUID id = this.journalEntryRepo.save(new JournalEntry(
                accountLedger,
                savingsAccountTransaction,
                false,
                JournalEntryType.fromValue(createJournalEntryReqDto.type()),
                createJournalEntryReqDto.amount(),
                createJournalEntryReqDto.description()
        )).getId();
        return new CreateJournalEntryResDTO(id);
    }

    @Transactional
    @Override
    public void postJournalEntry(PostJournalEntryReqDTO dto) throws PlatformException {
            final AccountLedger accountLedger = this.accountLedgerRepo.findByAccountType(AccountType.ASSET)
                    .orElseThrow(() -> new PlatformException("Account Ledger not found", HttpStatus.NOT_FOUND, "Account Ledger not found"));
            this.journalEntryRepo.save(
                    new JournalEntry(
                            accountLedger,
                            this.savingsAccountTransactionRepo.findById(dto.transactionId())
                                    .orElseThrow(() -> new PlatformException("Savings Account Transaction not found", HttpStatus.NOT_FOUND, "Savings Account Transaction not found")),
                            false,
                            dto.accountType(),
                            dto.amount(),
                            dto.description()
                    )
            );
    }


}
