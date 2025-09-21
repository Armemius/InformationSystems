package com.armemius.labwork.data.dto.objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;

    private Long ownerId;

    @NotNull
    private String street;

    @Size(max = 24)
    private String zipCode;

    @NotNull
    private Long townId;

}
