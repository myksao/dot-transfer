package com.dot.transfer.account.saving.repo;

import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface SavingsAccountTransactionRepo extends JpaRepository<SavingsAccountTransaction, UUID> {

    @Query("SELECT s FROM SavingsAccountTransaction s WHERE s.isReversed = :isReversed and s.commission is null and s.transactionFee is null")
    Page<SavingsAccountTransaction> fetchSuccessfulTransactions(@Param("isReversed") boolean isReversed, Pageable pageable);

    Page<SavingsAccountTransaction> findBySavingsAccountId(UUID savingsAccount_id, Pageable pageable);

    @Query("SELECT SUM(s.amount) FROM SavingsAccountTransaction s WHERE s.transactionDate BETWEEN :startDate AND :endDate AND s.isReversed = false AND s.transactionType = 100")
    Optional<BigDecimal> fetchTotalDepositForSpecificDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(s.amount) FROM SavingsAccountTransaction s WHERE s.transactionDate BETWEEN :startDate AND :endDate AND s.isReversed = false AND s.transactionType = 200")
    Optional<BigDecimal> fetchTotalWithdrawalForSpecificDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
