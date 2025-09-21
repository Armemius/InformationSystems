package com.armemius.labwork.data.domain;

import com.armemius.labwork.service.abstractions.Owned;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "organization")
@AllArgsConstructor
@NoArgsConstructor
public class Organization implements Owned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Min(value = 0)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    @NotNull
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @NotNull
    private Date creationDate = new Date();

    @ManyToOne
    @JoinColumn(name = "official_address_id")
    private Address officialAddress;

    @Column(name = "annual_turnover", nullable = false)
    @NotNull
    @Min(value = 0)
    private float annualTurnover;

    @Column(name = "employees_count", nullable = false)
    @NotNull
    @Min(value = 0)
    private int employeesCount;

    @Column(name = "rating", nullable = false)
    @NotNull
    @Min(value = 0)
    private Float rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrganizationType type;

    @ManyToOne
    @JoinColumn(name = "postal_address_id")
    private Address postalAddress;

}
