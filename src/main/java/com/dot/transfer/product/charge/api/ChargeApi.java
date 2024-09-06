package com.dot.transfer.product.charge.api;

import com.dot.transfer.infrastructure.common.ApiResponse;
import com.dot.transfer.product.charge.domain.Charge;
import com.dot.transfer.product.charge.dto.ChargeDTO;
import com.dot.transfer.product.charge.dto.CreateChargeReqDTO;
import com.dot.transfer.product.charge.dto.CreateChargeResDTO;
import com.dot.transfer.product.charge.dto.FetchChargesReqDTO;
import com.dot.transfer.product.charge.service.ChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/charges")
@Slf4j
@RequiredArgsConstructor
public class ChargeApi {

    private ChargeService chargeService;


    @Autowired
    public ChargeApi(ChargeService chargeService) {
        this.chargeService = chargeService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CreateChargeResDTO>> createCharge(@Valid @RequestBody CreateChargeReqDTO createChargeReqDTO) {
        log.info("Charge Created");
        return ResponseEntity.ok(ApiResponse.success(this.chargeService.createCharge(createChargeReqDTO)));
    }

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<ChargeDTO>>> getCharges(@Valid FetchChargesReqDTO fetchChargesReqDTO) {
        log.info("Charge Details");
        return ResponseEntity.ok(ApiResponse.success(this.chargeService.getCharges(fetchChargesReqDTO)));
    }

}
