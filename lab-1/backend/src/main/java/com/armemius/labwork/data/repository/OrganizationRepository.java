package com.armemius.labwork.data.repository;

import com.armemius.labwork.data.domain.Organization;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Page<Organization> findAllByName(@NotBlank String name, Pageable pageable);

    Long countAllByName(@NotBlank String name);
}
