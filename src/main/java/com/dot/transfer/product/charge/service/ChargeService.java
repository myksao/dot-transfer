package com.dot.transfer.product.charge.service;

import com.dot.transfer.product.charge.domain.Charge;
import com.dot.transfer.product.charge.dto.ChargeDTO;
import com.dot.transfer.product.charge.dto.CreateChargeReqDTO;
import com.dot.transfer.product.charge.dto.CreateChargeResDTO;
import com.dot.transfer.product.charge.dto.FetchChargesReqDTO;
import org.springframework.data.domain.Page;

public interface ChargeService {

    CreateChargeResDTO createCharge(CreateChargeReqDTO createChargeReqDTO);

    Page<ChargeDTO> getCharges(FetchChargesReqDTO fetchChargesReqDTO);
}
