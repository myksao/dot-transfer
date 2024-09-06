package com.dot.transfer.product.charge.service;


import com.dot.transfer.product.charge.domain.Charge;
import com.dot.transfer.product.charge.dto.ChargeDTO;
import com.dot.transfer.product.charge.dto.CreateChargeReqDTO;
import com.dot.transfer.product.charge.dto.CreateChargeResDTO;
import com.dot.transfer.product.charge.dto.FetchChargesReqDTO;
import com.dot.transfer.product.charge.repo.ChargeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChargeServiceImpl implements ChargeService{

    private ChargeRepo chargeRepo;

    @Autowired
    public ChargeServiceImpl(ChargeRepo chargeRepo) {
        this.chargeRepo = chargeRepo;
    }


    @Override
    public CreateChargeResDTO createCharge(CreateChargeReqDTO createChargeReqDTO) {
        final UUID id = this.chargeRepo.save(createChargeReqDTO.toDomain()).getId();
        return new CreateChargeResDTO(id);
    }

    @Override
    public Page<ChargeDTO> getCharges(FetchChargesReqDTO fetchChargesReqDTO) {
        Pageable pageable = PageRequest.of(fetchChargesReqDTO.page(), fetchChargesReqDTO.size());

        return this.chargeRepo.findAll(pageable).map(charge -> new ChargeDTO(
                        charge.getId(),
                        charge.getName(),
                        charge.getDescription(),
                        charge.getCode(),
                        charge.getType(),
                        charge.getCommission(),
                        charge.getTransactionFee(),
                        charge.getCap(),
                        charge.getCurrency(),
                        charge.getIsDeleted(),
                        charge.getCreatedDate()
                ));
    }
}
