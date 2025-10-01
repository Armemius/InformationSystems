package com.armemius.labwork.data.dto.objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDto {

    private Long id;

    private Long ownerId;

    @Min(value = -469)
    private Float x;

    @NotNull
    @Min(value = -440)
    private Long y;

}
