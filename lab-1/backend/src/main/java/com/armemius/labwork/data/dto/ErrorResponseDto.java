package com.armemius.labwork.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    @NotNull
    private String error;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private int status;
}
