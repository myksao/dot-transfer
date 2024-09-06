CREATE SCHEMA IF NOT EXISTS dot;

CREATE TABLE dot.account_ledger
(
    id             UUID NOT NULL,
    version        BIGINT,
    name           VARCHAR(255),
    account_number VARCHAR(255),
    account_type   INTEGER,
    created_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by     VARCHAR(255),
    CONSTRAINT pk_account_ledger PRIMARY KEY (id)
);

CREATE TABLE dot.account_ledger_entry
(
    account_ledger_id UUID NOT NULL,
    entry_id          UUID NOT NULL
);

CREATE TABLE dot.charge
(
    id                UUID NOT NULL,
    version           BIGINT,
    name              VARCHAR(255),
    description       VARCHAR(255),
    charge_code       VARCHAR(255),
    charge_apply_type INTEGER,
    commission        DECIMAL,
    transaction_fee   DECIMAL,
    cap               DECIMAL,
    currency          INTEGER,
    is_deleted        BOOLEAN,
    created_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by        VARCHAR(255),
    CONSTRAINT pk_charge PRIMARY KEY (id)
);

CREATE TABLE dot.journal_entry
(
    id             UUID NOT NULL,
    version        BIGINT,
    description    VARCHAR(255),
    transaction_id UUID,
    ledger_id      UUID,
    is_reversed    BOOLEAN DEFAULT FALSE,
    type           INTEGER,
    amount         DECIMAL,
    created_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by     VARCHAR(255),
    CONSTRAINT pk_journal_entry PRIMARY KEY (id)
);

CREATE TABLE dot.savings_account
(
    id               UUID NOT NULL,
    version          BIGINT,
    account_number   VARCHAR(255),
    balance          DECIMAL DEFAULT 0,
    total_deposit    DECIMAL DEFAULT 0,
    total_withdrawal DECIMAL DEFAULT 0,
    status           INTEGER,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    charge_id        UUID,
    CONSTRAINT pk_savings_account PRIMARY KEY (id)
);

CREATE TABLE dot.savings_account_transaction
(
    id                      UUID NOT NULL,
    version                 BIGINT,
    savings_account_id      UUID NOT NULL,
    is_reversed             BOOLEAN DEFAULT FALSE,
    transaction_type        INTEGER,
    transaction_date        TIMESTAMP WITHOUT TIME ZONE,
    amount                  DECIMAL,
    commission              DECIMAL,
    transaction_fee         DECIMAL,
    note                    VARCHAR(255),
    transaction_description VARCHAR(255),
    transaction_status      VARCHAR(255),
    created_at              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_savings_account_transaction PRIMARY KEY (id)
);

CREATE TABLE dot.savings_account_transaction_journal_entry
(
    savings_account_transaction_id UUID NOT NULL,
    journal_entry_id               UUID NOT NULL,
    CONSTRAINT pk_savings_account_transaction_journalentry PRIMARY KEY (savings_account_transaction_id, journal_entry_id)
);

ALTER TABLE dot.account_ledger_entry
    ADD CONSTRAINT uc_account_ledger_entry_entry UNIQUE (entry_id);

ALTER TABLE dot.savings_account
    ADD CONSTRAINT uc_savings_account_account_number UNIQUE (account_number);

ALTER TABLE dot.savings_account_transaction_journal_entry
    ADD CONSTRAINT uc_savings_account_transaction_journal_entry_journalentry UNIQUE (journal_entry_id);

ALTER TABLE dot.journal_entry
    ADD CONSTRAINT FK_JOURNAL_ENTRY_ON_LEDGER FOREIGN KEY (ledger_id) REFERENCES dot.account_ledger (id);

ALTER TABLE dot.journal_entry
    ADD CONSTRAINT FK_JOURNAL_ENTRY_ON_TRANSACTION FOREIGN KEY (transaction_id) REFERENCES dot.savings_account_transaction (id);

ALTER TABLE dot.savings_account
    ADD CONSTRAINT FK_SAVINGS_ACCOUNT_ON_CHARGE FOREIGN KEY (charge_id) REFERENCES dot.charge (id);

ALTER TABLE dot.savings_account_transaction
    ADD CONSTRAINT FK_SAVINGS_ACCOUNT_TRANSACTION_ON_SAVINGS_ACCOUNT FOREIGN KEY (savings_account_id) REFERENCES dot.savings_account (id);

ALTER TABLE dot.account_ledger_entry
    ADD CONSTRAINT fk_accledent_on_account_ledger FOREIGN KEY (account_ledger_id) REFERENCES dot.account_ledger (id);

ALTER TABLE dot.account_ledger_entry
    ADD CONSTRAINT fk_accledent_on_journal_entry FOREIGN KEY (entry_id) REFERENCES dot.journal_entry (id);

ALTER TABLE dot.savings_account_transaction_journal_entry
    ADD CONSTRAINT fk_savacctrajouent_on_journal_entry FOREIGN KEY (journal_entry_id) REFERENCES dot.journal_entry (id);

ALTER TABLE dot.savings_account_transaction_journal_entry
    ADD CONSTRAINT fk_savacctrajouent_on_savings_account_transaction FOREIGN KEY (savings_account_transaction_id) REFERENCES dot.savings_account_transaction (id);