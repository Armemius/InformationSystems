package com.armemius.labwork.data.repository;

import com.armemius.labwork.data.domain.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {}
