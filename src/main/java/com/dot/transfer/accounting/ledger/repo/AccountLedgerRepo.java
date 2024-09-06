package com.dot.transfer.accounting.ledger.repo;

import com.dot.transfer.accounting.ledger.constant.AccountType;
import com.dot.transfer.accounting.ledger.domain.AccountLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountLedgerRepo extends JpaRepository<AccountLedger, UUID> {

    Optional<AccountLedger> findByAccountType(AccountType accountType);
}

