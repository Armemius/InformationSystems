package com.armemius.labwork.data.repository;

import com.armemius.labwork.data.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {}
