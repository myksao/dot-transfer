package com.dot.transfer.account.saving.domain;

import com.dot.transfer.account.saving.constant.SavingsAccountTransactionType;
import com.dot.transfer.account.saving.constant.SavingsAccountTransactionTypeConverter;
import com.dot.transfer.accounting.journalentry.domain.JournalEntry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(schema = "dot", name = "savings_account_transaction")
@Getter
@Setter
public class SavingsAccountTransaction {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    @Column(name ="version")
    private Long version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "savings_account_id", referencedColumnName = "id", nullable = false)
    private SavingsAccount savingsAccount;


    @Column(name ="is_reversed", columnDefinition = "boolean default false")
    private Boolean isReversed;


    @Convert(converter = SavingsAccountTransactionTypeConverter.class)
    @Column(name ="transaction_type")
    private SavingsAccountTransactionType transactionType;

    @Column(name ="transaction_date")
    private Date transactionDate;

    @Column(name ="amount")
    private BigDecimal amount;

    @Column(name ="commission")
    private BigDecimal commission;

    @Column(name ="transaction_fee")
    private BigDecimal transactionFee;

    @Column(name ="note")
    private String note;

    @Column(name ="transaction_description")
    private String transactionDescription;

    @Column(name ="transaction_status")
    private String transactionStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="created_at", nullable = false)
    @CreationTimestamp
    private Date createdDate;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="updated_at")
    @LastModifiedDate
    private Date updatedDate;


    @OneToMany
    private Set<JournalEntry> journalEntry = new HashSet<>();

    @Transient
    private BigDecimal totalCharges;

}
