package com.dot.transfer.accounting.ledger.service;

import com.dot.transfer.accounting.ledger.dto.CreateAccountLedgerReqDTO;
import com.dot.transfer.accounting.ledger.dto.CreateAccountLedgerResDTO;
import com.dot.transfer.accounting.ledger.dto.FetchAcctLedgerReqDTO;
import com.dot.transfer.accounting.ledger.dto.LedgerDTO;
import org.springframework.data.domain.Page;

public interface AccountLedgerService {
    CreateAccountLedgerResDTO createAccountLedger(CreateAccountLedgerReqDTO reqDTO);

    Page<LedgerDTO> getAccountLedgers(FetchAcctLedgerReqDTO fetchAcctLedgerReqDTO);
}
