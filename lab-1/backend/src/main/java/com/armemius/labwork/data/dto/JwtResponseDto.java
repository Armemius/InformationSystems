package com.armemius.labwork.data.dto;

import com.armemius.labwork.data.domain.SystemUserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {

    @NotNull
    SystemUserRole role;

    @NotNull
    String username;

    @NotBlank
    String token;

}
