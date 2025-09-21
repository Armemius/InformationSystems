package com.armemius.labwork.data.repository;

import com.armemius.labwork.data.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {}
