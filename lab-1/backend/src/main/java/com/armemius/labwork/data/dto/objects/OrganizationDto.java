package com.armemius.labwork.data.dto.objects;

import com.armemius.labwork.data.domain.OrganizationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {

    private Long id;

    private Long ownerId;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Long coordinatesId;

    @NotNull
    private Date creationDate = new Date();

    private Long officialAddressId;

    @NotNull
    @Min(value = 1)
    private Float annualTurnover;

    @NotNull
    @Min(value = 1)
    private Integer employeesCount;

    @NotNull
    @Min(value = 1)
    private Float rating;

    private OrganizationType type;

    private Long postalAddressId;

}
