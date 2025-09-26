package com.armemius.labwork.data.repository;

import com.armemius.labwork.data.domain.AppUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findFirstByUsername(@NotBlank @Size(min = 5, max = 16) String username);
}
