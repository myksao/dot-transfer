package com.dot.transfer.accounting.ledger.domain;


import com.dot.transfer.accounting.journalentry.domain.JournalEntry;
import com.dot.transfer.accounting.ledger.constant.AccountType;
import com.dot.transfer.accounting.ledger.constant.AccountTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Table(schema = "dot", name = "account_ledger")
@NoArgsConstructor
@Getter
public class AccountLedger {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    @Column(name ="version")
    private Long version;

    @Column(name ="name")
    private String name;

    @Column(name ="account_number")
    private String accountNumber;

    @Convert(converter = AccountTypeConverter.class)
    @Column(name ="account_type")
    private AccountType accountType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name ="created_by")
    private String createdBy;

    @OneToMany
    protected List<JournalEntry> entry = new ArrayList<>();


    public AccountLedger(String name, String accountNumber, AccountType accountType) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

}
