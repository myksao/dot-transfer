package com.dot.transfer.product.charge.domain;

import com.dot.transfer.account.saving.domain.SavingsAccount;
import com.dot.transfer.infrastructure.common.CurrencyConverter;
import com.dot.transfer.infrastructure.common.CurrencyType;
import com.dot.transfer.product.charge.constant.ChargeApplyType;
import com.dot.transfer.product.charge.constant.ChargeApplyTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@AllArgsConstructor
@Entity
@Table(schema = "dot", name = "charge")
@NoArgsConstructor
@Getter
public class Charge {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    @Column(name ="version")
    private Long version;

    @Column(name ="name")
    private String name;

    @Column(name ="description")
    private String description;

    @Column(name ="charge_code")
    private String code;

    @Convert(converter = ChargeApplyTypeConverter.class)
    @Column(name ="charge_apply_type")
    private ChargeApplyType type;

    @Column(name ="commission")
    private BigDecimal commission;

    @Column(name ="transaction_fee")
    private BigDecimal transactionFee;

    @Column(name ="cap")
    private BigDecimal cap;

    @Convert(converter = CurrencyConverter.class)
    @Column(name ="currency")
    private CurrencyType currency;

    @Column(name ="is_deleted")
    private Boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name ="created_by")
    private String createdBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "charge", orphanRemoval = true, fetch = FetchType.LAZY)
    protected Set<SavingsAccount> accounts = new HashSet<>();


    public Charge(String name, String description, String code, ChargeApplyType type, BigDecimal commission, BigDecimal transactionFee, BigDecimal cap, CurrencyType currency) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.type = type;
        this.currency = currency;
        this.commission = commission;
        this.transactionFee = transactionFee;
        this.cap = cap;
    }

    public Charge(String name, String description, String code, ChargeApplyType type, BigDecimal value, CurrencyType currency) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.type = type;
        this.currency = currency;
    }
}
