package com.dot.transfer.account.saving.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.RequestParam;

public record TransactionSummaryReqDTO(
        @RequestParam(name = "date")
        @NotNull(
                message = "Start Date is required"
        )
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in the format yyyy-MM-dd")
        String start_date,

        @NotNull(
                message = "End Date is required"
        )
        @RequestParam(name = "date")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in the format yyyy-MM-dd")
        String end_date

) {
}
