package com.armemius.labwork.data.repository;

import com.armemius.labwork.data.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {}
