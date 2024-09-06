package com.dot.transfer.product.charge.repo;

import com.dot.transfer.product.charge.domain.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChargeRepo  extends JpaRepository<Charge, UUID> {

}