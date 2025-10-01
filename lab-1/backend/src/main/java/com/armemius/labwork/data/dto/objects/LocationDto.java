package com.armemius.labwork.data.dto.objects;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private Long id;

    private Long ownerId;

    @NotNull
    private Integer x;

    @NotNull
    private Float y;

    @NotNull
    private Double z;

}
