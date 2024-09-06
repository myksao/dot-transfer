package com.dot.transfer.accounting.ledger.service;


import com.dot.transfer.accounting.ledger.domain.AccountLedger;
import com.dot.transfer.accounting.ledger.dto.CreateAccountLedgerReqDTO;
import com.dot.transfer.accounting.ledger.dto.CreateAccountLedgerResDTO;
import com.dot.transfer.accounting.ledger.dto.FetchAcctLedgerReqDTO;
import com.dot.transfer.accounting.ledger.dto.LedgerDTO;
import com.dot.transfer.accounting.ledger.repo.AccountLedgerRepo;
import com.dot.transfer.product.charge.domain.Charge;
import com.dot.transfer.product.charge.dto.FetchChargesReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountLedgerServiceImpl implements  AccountLedgerService {

    AccountLedgerRepo accountLedgerRepo;

    @Autowired
    public AccountLedgerServiceImpl(AccountLedgerRepo accountLedgerRepo) {
        this.accountLedgerRepo = accountLedgerRepo;
    }


    @Override
    public CreateAccountLedgerResDTO createAccountLedger(CreateAccountLedgerReqDTO reqDTO) {
        final UUID id = this.accountLedgerRepo.save(reqDTO.toDomain()).getId();
        return new CreateAccountLedgerResDTO(id);
    }

    @Override
    public Page<LedgerDTO> getAccountLedgers(FetchAcctLedgerReqDTO fetchAcctLedgerReqDTO) {
        Pageable pageable = PageRequest.of(fetchAcctLedgerReqDTO.page(), fetchAcctLedgerReqDTO.size());

        return this.accountLedgerRepo.findAll(pageable).map(
                accountLedger -> new LedgerDTO(
                        accountLedger.getId(),
                        accountLedger.getName(),
                        accountLedger.getAccountNumber(),
                        accountLedger.getAccountType(),
                        accountLedger.getCreatedDate(),
                        accountLedger.getCreatedBy()
                ));
    }
}
