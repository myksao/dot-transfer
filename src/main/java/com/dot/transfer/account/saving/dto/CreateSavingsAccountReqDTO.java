package com.dot.transfer.account.saving.dto;

import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateSavingsAccountReqDTO(
        @Pattern(regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Invalid UUID format")
        String  charge_id
) {
}
