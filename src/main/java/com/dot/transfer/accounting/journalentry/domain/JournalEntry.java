package com.dot.transfer.accounting.journalentry.domain;


import com.dot.transfer.account.saving.domain.SavingsAccountTransaction;
import com.dot.transfer.accounting.journalentry.constant.JournalEntryType;
import com.dot.transfer.accounting.journalentry.constant.JournalEntryTypeConverter;
import com.dot.transfer.accounting.ledger.domain.AccountLedger;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Table(schema = "dot", name = "journal_entry")
@NoArgsConstructor
@Getter
@Setter
public class JournalEntry {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    @Column(name ="version")
    private Long version;

    @Column(name ="description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private SavingsAccountTransaction transaction;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ledger_id", nullable = true)
    private AccountLedger ledgers;


    @Column(name ="is_reversed", columnDefinition = "boolean default false")
    private Boolean isReversed;

    @Convert(converter = JournalEntryTypeConverter.class)
    @Column(name ="type")
    private JournalEntryType type;

    @Column(name ="amount")
    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name ="created_by")
    private String createdBy;






    public JournalEntry( AccountLedger ledgers, SavingsAccountTransaction transaction, Boolean isReversed, JournalEntryType type, BigDecimal amount, String description) {
        this.ledgers = ledgers;
        this.transaction = transaction;
        this.isReversed = isReversed;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

}
