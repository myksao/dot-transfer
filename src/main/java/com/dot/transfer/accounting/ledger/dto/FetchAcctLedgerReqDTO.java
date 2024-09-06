package com.dot.transfer.accounting.ledger.dto;

import org.springframework.web.bind.annotation.RequestParam;

public record FetchAcctLedgerReqDTO(
        @RequestParam(value = "page", defaultValue = "100")
        int page,
        @RequestParam(value = "size", defaultValue = "100")
        int size
) {
}
