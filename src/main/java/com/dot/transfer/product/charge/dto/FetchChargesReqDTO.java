package com.dot.transfer.product.charge.dto;

import org.springframework.web.bind.annotation.RequestParam;

public record FetchChargesReqDTO(
        @RequestParam(value = "page", defaultValue = "100")
        int page,
        @RequestParam(value = "size", defaultValue = "100")
                                 int size
) {
}