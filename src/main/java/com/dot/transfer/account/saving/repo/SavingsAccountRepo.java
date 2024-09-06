package com.dot.transfer.account.saving.repo;

import com.dot.transfer.account.saving.domain.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface SavingsAccountRepo extends JpaRepository<SavingsAccount, UUID> {

    Optional<SavingsAccount> findByAccountNumber(@Param("account_no") String account_no);
}

