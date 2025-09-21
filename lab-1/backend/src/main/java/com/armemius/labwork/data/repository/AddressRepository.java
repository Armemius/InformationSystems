package com.armemius.labwork.data.repository;

import com.armemius.labwork.data.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {}
