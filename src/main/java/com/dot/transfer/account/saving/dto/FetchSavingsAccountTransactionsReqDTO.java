package com.dot.transfer.account.saving.dto;

import org.springframework.web.bind.annotation.RequestParam;

public record FetchSavingsAccountTransactionsReqDTO(
        @RequestParam(value = "page", defaultValue = "100")
        int page,
        @RequestParam(value = "size", defaultValue = "100")
        int size,
        @RequestParam(value = "account_no", required = true)
        String account_no
) {
}
