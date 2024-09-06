package com.dot.transfer.account.saving.domain;


import com.dot.transfer.account.saving.constant.SavingsAccountStatusType;
import com.dot.transfer.account.saving.constant.SavingsAccountStatusTypeConverter;
import com.dot.transfer.account.saving.constant.SavingsAccountTransactionType;
import com.dot.transfer.account.saving.dto.SavingsAccountDTO;
import com.dot.transfer.product.charge.domain.Charge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Entity
@Table(schema = "dot", name = "savings_account")
@NoArgsConstructor
@Getter
@Setter
public class SavingsAccount {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    @Column(name ="version")
    private Long version;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    @ColumnDefault("0.0")
    @Column(name = "balance")
    private BigDecimal balance;

    @ColumnDefault("0.0")
    @Column(name ="total_deposit")
    private BigDecimal totalDeposit;

    @ColumnDefault("0.0")
    @Column(name ="total_withdrawal")
    private BigDecimal totalWithdrawal;

    @Convert(converter = SavingsAccountStatusTypeConverter.class)
    @Column(name ="status")
    private SavingsAccountStatusType status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="created_at", nullable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name ="updated_at")
    @LastModifiedDate
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "charge_id", referencedColumnName = "id")
    private Charge charge;

    @OrderBy(value = "transactionDate DESC")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "savingsAccount", orphanRemoval = true, fetch = FetchType.LAZY)
    protected List<SavingsAccountTransaction> transactions = new ArrayList<>();

    public SavingsAccount(String accountNumber, Charge charge) {
        this.accountNumber = accountNumber;
        this.charge = charge;
        this.status= SavingsAccountStatusType.ACTIVE;
        this.totalDeposit = BigDecimal.ZERO;
        this.totalWithdrawal = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
    }


    public void addTransaction(final SavingsAccountTransaction transaction) {
        this.transactions.add(transaction);
    }


    public BigDecimal calculateTotalDeposit() {
        return this.getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType().equals(SavingsAccountTransactionType.DEPOSIT))
                .map(SavingsAccountTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public  BigDecimal calculateTotalCommission() {
        return this.getTransactions().stream()
                .map(SavingsAccountTransaction::getCommission)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalTransactionFee() {
        return this.getTransactions().stream()
                .map(SavingsAccountTransaction::getTransactionFee)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalWithdrawal() {
        return this.getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType().equals(SavingsAccountTransactionType.WITHDRAWAL))
                .map(SavingsAccountTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public SavingsAccountDTO toDTO(){
        return new SavingsAccountDTO(
               this.id,
               this.accountNumber,
               this.balance,
               this.totalDeposit,
               this.totalWithdrawal,
               this.status,
               this.createdDate
        );
    }

}
