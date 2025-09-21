package com.armemius.labwork.data.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "street", nullable = false)
    @NotBlank
    private String street;

    @Column(name = "zip_code")
    @Size(max = 24)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "town_id", nullable = false)
    @NotNull
    private Location town;

    @OneToMany(mappedBy = "officialAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Organization> correspondingOrganizationsWithOfficialAddress = new ArrayList<>();

    @OneToMany(mappedBy = "postalAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Organization> correspondingOrganizationsWithPostalAddress = new ArrayList<>();

}
